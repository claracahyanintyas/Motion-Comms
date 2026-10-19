import { useParams, useSearchParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import TransactionForm from "../components/TransactionForm";
import CommissionOrderService from "../services/CommissionOrderService";
import toast from "react-hot-toast";

export default function TransactionPage() {
  const { id } = useParams(); // order ID
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const paymentType = searchParams.get("type"); // DOWN_PAYMENT or REMAINING_PAYMENT

  useEffect(() => {
    const fetchOrder = async () => {
      try {
        const res = await CommissionOrderService.getCommissionOrderDetail(id);
        setOrder(res.data.commissionOrder);
      } catch (error) {
        const message = error.response?.data?.message || error.message || "Something went wrong"
        toast.error(message);
      } finally {
        setLoading(false);
      }
    };
    fetchOrder();
  }, [id]);

  if (loading) return <p>Loading...</p>;
  if (!order) return <p>Order not found</p>;

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <TransactionForm
        order={order}
        paymentType={paymentType}
        onSuccess={() => navigate(`/commission-orders/${order.id}`)}
      />
    </div>
  );
}
