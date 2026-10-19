import React from "react";
import CommissionOrderService from "../services/CommissionOrderService";
import TransactionService from "../services/TransactionService";
import toast from "react-hot-toast";
import { formatDateTime } from "../utils/date";

export default function Transaction({ transaction, isSeller, onConfirmed }) {

  const confirmPayment = async () => {
    try {
      await TransactionService.confirmTransaction(transaction.id);
      onConfirmed(); // refresh list
    } catch (err) {
      const message = err.response.data.message || err.message || "Something went wrong"
      toast.error(message);
    }
  };

  return (
    <div className="border rounded-lg p-4 bg-white shadow-sm">
      <div className="flex flex-col justify-between items-center mb-2">
        <h3 className="font-medium">
          {transaction.type === "DOWN_PAYMENT"
            ? "Down Payment"
            : "Remaining Payment"}
        </h3>

        <span className="text-sm text-gray-500">
          {formatDateTime(transaction.createdAt)}
        </span>
      </div>

      <p>
        Amount: <strong>{transaction.amount}</strong>
      </p>

      <p>
        Status:{" "}
        <strong className={
          transaction.status === "CONFIRMED"
            ? "text-green-600"
            : "text-yellow-600"
        }>
          {transaction.status}
        </strong>
      </p>
      <div className="flex flex-col">
      {transaction.proofUrl && (
        <a
          href={transaction.proofUrl}
          target="_blank"
          rel="noopener noreferrer"
          className="text-blue-500 underline mt-2 inline-block"
        >
          View Proof
        </a>
      )}

      {/* Seller confirms payment */}
      {isSeller && transaction.status === "PENDING" && (
        <button
          onClick={confirmPayment}
          className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]"
        >
          Confirm Payment Received
        </button>
      )}
      </div>
    </div>
  );
}
