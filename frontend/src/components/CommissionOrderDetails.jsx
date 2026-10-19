import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from "react-router-dom";
import CommissionOrderService from '../services/CommissionOrderService';
import Transaction from './Transaction';
import MediaBox from './MediaBox';
import { formatDateTime } from '../utils/date'
import toast from 'react-hot-toast';

export default function CommissionOrderDetails() {

  const { id } = useParams(); 
  const [order, setOrder] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  const loggedInUser = JSON.parse(localStorage.getItem("user"));

  // Determine roles once order is loaded
  let isBuyer = false;
  let isSeller = false;

  const goToTransaction = (type) => {
  navigate(`/transaction/${order.id}?type=${type}`);
  };

  useEffect(() => {
    fetchOrder();
  }, [id]);
useEffect(() => {
  if (order) {
    fetchTransactions();
  }
}, [order]);

const fetchTransactions = async () => {
  try {
    const res = await CommissionOrderService.getTransactionsByOrder(order.id);
    setTransactions(res.data.transactions);
  } catch (error) {
      const message = error.response?.data?.message || error.message || "Something went wrong"
      toast.error(message);
  } finally {
    setLoading(false);
  }
};


  const fetchOrder = async () => {
    try {
      const res = await CommissionOrderService.getCommissionOrderDetail(id);
      setOrder(res.data.commissionOrder);
    } catch (err) {
      toast.error('Error fetching commission order', err);
    }
  };

  if (order) {
  isBuyer = order.buyer.id === loggedInUser.id;
  isSeller = order.seller.id === loggedInUser.id;
  }

{/* Down Payment */}
{isBuyer && order.status === "PENDING_DOWN_PAYMENT" && !order.downPaymentPaid && (
  <button
    className="bg-blue-500 text-white p-2 rounded-md"
    onClick={() => goToTransaction("DOWN_PAYMENT")}
  >
    Pay Down Payment
  </button>
)}

{/* Full Payment */}
{isBuyer && (order.status === "PENDING_REMAINING_PAYMENT" || order.status === "IN_PROGRESS") && !order.fullPaymentPaid && (
  <button
    className="bg-green-500 text-white p-2 rounded-md"
    onClick={() => goToTransaction("REMAINING_PAYMENT")}
  >
    Pay Full Payment
  </button>
)}
  const confirmAsBuyer = async () => {
  try {
    await CommissionOrderService.confirmAsBuyer(order.id);
    // refresh order
    const res = await CommissionOrderService.getCommissionOrderDetail(order.id);
    setOrder(res.data.commissionOrder);
  } catch (err) {
    toast.error(err);
  }
};
  const confirmAsSeller = async () => {
  try {
    await CommissionOrderService.confirmAsSeller(order.id);
    // refresh order
    const res = await CommissionOrderService.getCommissionOrderDetail(order.id);
    setOrder(res.data.commissionOrder);
  } catch (err) {
    toast.error(err);
  }
};

  if (!order) return <p>Loading...</p>;

  return (
    <div className="flex flex-col items-center p-4 w-full text-[#2F4156] ">
<div className="w-4/5 max-w-4xl bg-white rounded-lg shadow-xl p-6 space-y-6">

  {/* Header */}
  <div>
    <h1 className="text-2xl font-semibold">
      Order #{order.id}
    </h1>
    <p className="text-gray-600">
      {order.commission.title}
    </p>
  </div>

  {/* Media */}
  <div className="flex justify-center p-3 w-2/5 mx-auto">
    <MediaBox url="http://localhost:9100/transaction-proofs/31540f4d-17cd-4fc3-bf05-7b2b28bdab6d" />
  </div>

  {/* Status + Payments */}
  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

    {/* Confirmation statuses */}
    <div className="space-y-2">
      <h2 className="font-semibold text-lg">Confirmations</h2>

      <p>
        Down Payment:{' '}
        <span className={order.downPaymentPaid ? 'text-green-600' : 'text-red-600'}>
          {order.downPaymentPaid ? 'Paid ✅' : 'Unpaid ❌'}
        </span>
      </p>

      <p>
        Full Payment:{' '}
        <span className={order.fullPaymentPaid ? 'text-green-600' : 'text-red-600'}>
          {order.fullPaymentPaid ? 'Paid ✅' : 'Unpaid ❌'}
        </span>
      </p>

      <p>
        Buyer Confirmed:{' '}
        <span className={order.confirmedByBuyer ? 'text-green-600' : 'text-red-600'}>
          {order.confirmedByBuyer ? 'Yes ✅' : 'No ❌'}
        </span>
      </p>

      <p>
        Seller Confirmed:{' '}
        <span className={order.confirmedBySeller ? 'text-green-600' : 'text-red-600'}>
          {order.confirmedBySeller ? 'Yes ✅' : 'No ❌'}
        </span>
      </p>
    </div>

    {/* Prices */}
    <div className="space-y-2">
      <h2 className="font-semibold text-lg">Payment Breakdown</h2>

      <div className="flex justify-between">
        <span>Down Payment</span>
        <span>€{order.downPaymentAmount}</span>
      </div>

      <div className="flex justify-between">
        <span>Remaining Payment</span>
        <span>€{order.remainingPaymentAmount}</span>
      </div>

      <div className="flex justify-between font-semibold border-t pt-2">
        <span>Total Price</span>
        <span>€{order.totalPrice}</span>
      </div>
    </div>
  </div>

  {/* Footer */}
  <div className="border-t pt-4 text-sm text-gray-600 space-y-1">
    <p>Started at: {formatDateTime(order.createdAt)}</p>
    <p>
      Seller: @{order.seller.username} · Buyer: @{order.buyer.username}
    </p>
    <p className="font-medium text-gray-800">
      Status: {order.status}
    </p>
  </div>

</div>

  {/* Down Payment */}
  {isBuyer && order.status === "PENDING_DOWN_PAYMENT" && !order.downPaymentPaid && (
    <button
      className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]"
      onClick={() => goToTransaction("DOWN_PAYMENT")}
    >
      Pay Down Payment
    </button>
  )}

  {/* Full Payment */}
  {isBuyer && (order.status === "PENDING_REMAINING_PAYMENT" || order.status === "IN_PROGRESS") && !order.fullPaymentPaid && (
    <button
      className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]"
      onClick={() => goToTransaction("REMAINING_PAYMENT")}
    >
      Pay Full Payment
    </button>
  )}

    {/* Confirm as Buyer */}
    {isBuyer && (order.status === "PENDING_REMAINING_PAYMENT" || order.status === "AWAITING_CONFIRMATION" || order.status === "IN_PROGRESS") && !order.confirmedByBuyer && (
      <button 
      className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]"
      onClick={confirmAsBuyer}>Confirm Received</button>
    )}

    {/* Confirm as Seller */}
    {isSeller && (order.status === "AWAITING_CONFIRMATION" || order.status === "IN_PROGRESS") && !order.confirmedBySeller && (
      <button
      className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]"
      onClick={confirmAsSeller}>Confirm Delivered</button>
    )}
    <div className="mt-6 space-y-4">
  <h2 className="text-xl font-semibold">Transactions</h2>

  {transactions.length === 0 && (
    <p className="text-gray-500">No transactions yet</p>
  )}

<div className='flex space-x-2'>
  {transactions.map((tx) => (
    <Transaction
      key={tx.id}
      transaction={tx}
      isSeller={isSeller}
      onConfirmed={fetchTransactions}
    />
  ))}
  </div>
</div>

    </div>
    
  )
}
