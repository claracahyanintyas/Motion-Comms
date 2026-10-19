import { useState, useEffect } from "react";
import CommissionOrderService from "../services/CommissionOrderService";
import PayoutMethodService from "../services/PayoutMethodService";
import toast from "react-hot-toast";

const TransactionForm = ({ order, paymentType, onSuccess }) => {
  const [proofFile, setProofFile] = useState(null);
  const [payoutMethods, setPayoutMethods] = useState([]);
  const [selectedPayoutId, setSelectedPayoutId] = useState("");
  const [errors, setErrors] = useState({
    proofFile: "",
    payoutMethod: "",
  });
  const [loading, setLoading] = useState(false);
  const [loadingPayouts, setLoadingPayouts] = useState(true);

  useEffect(() => {
    // Fetch seller's active payout methods
    const fetchPayoutMethods = async () => {
      try {
        setLoadingPayouts(true);
        const res = await PayoutMethodService.getActivePayoutMethodsByUser(order.seller.id);
        setPayoutMethods(res.data.payoutMethods);
        if (res.data.length > 0) setSelectedPayoutId(res.data[0].id);
      } catch (err) {
        console.error("Error fetching payout methods", err);
      } finally {
        setLoadingPayouts(false);
      }
    };
    fetchPayoutMethods();
  }, [order.seller.id]);

  const validateForm = () => {
    let valid = true;
    const errorsCopy = { proofFile: "", payoutMethod: "" };

    if (!proofFile) {
      errorsCopy.proofFile = "Proof file is required";
      valid = false;
    }

    if (!selectedPayoutId) {
      errorsCopy.payoutMethod = "Please select a payout method";
      valid = false;
    }

    setErrors(errorsCopy);
    return valid;
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("proofFile", proofFile)
      formData.append("payoutMethodId", selectedPayoutId)
      formData.append("type", paymentType)
      formData.append("amount", paymentType === "DOWN_PAYMENT" ? order.downPaymentAmount : order.remainingPaymentAmount)

      await CommissionOrderService.pay(formData, order.id);

      setProofFile(null);
      if (onSuccess) onSuccess();
    } catch (err) {
      const message = err.response.data.message || err.message || "Something went wrong"
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  if (loadingPayouts) {
  return <p className="text-center">Loading payout methods...</p>;
}
  return (
    <div className="flex flex-col items-center justify-center p-4 w-full text-[#2F4156]">
      <form
        onSubmit={handleUpload}
        className="flex flex-col w-1/2 p-4 bg-gray-50 rounded-lg border border-gray-300 space-y-4"
      >
        <h3 className="text-2xl font-semibold text-center mb-2">
          {paymentType === "DOWN_PAYMENT" ? "Pay Down Payment" : "Pay Remaining Payment"}
        </h3>

        {/* Payout Method */}
        <div className="flex flex-col">
          <label className="mb-1 font-medium">Select Payout Method</label>
          <select
            className="border-2 border-[#C8D9E6] p-2 rounded-md"
            value={selectedPayoutId}
            onChange={(e) => setSelectedPayoutId(e.target.value)}
          >
            <option value="">Select payout method...</option>
            {payoutMethods.map((pm) => (
              <option key={pm.id} value={pm.id}>
                {pm.type} - {pm.providerName} : {pm.accountName} - {pm.accountNumber} 
              </option>
            ))}
          </select>
          {errors.payoutMethod && (
            <div className="text-red-500 text-sm mt-1">{errors.payoutMethod}</div>
          )}
        </div>

        {/* Proof File */}
        <div className="flex flex-col">
          <label className="mb-1 font-medium">Upload Proof</label>
          <input
            type="file"
            onChange={(e) => setProofFile(e.target.files[0])}
            className="border-2 border-[#C8D9E6] p-2 rounded-md"
          />
          {errors.proofFile && (
            <div className="text-red-500 text-sm mt-1">{errors.proofFile}</div>
          )}
        </div>

        {/* Amount Display */}
        <div className="text-lg font-medium">
          Amount: {paymentType === "DOWN_PAYMENT" ? order.downPaymentAmount : order.remainingPaymentAmount}
        </div>

        <button
          type="submit"
          className={`bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]`}
          disabled={loading}
        >
          {loading ? "Uploading..." : "Upload Payment"}
        </button>
      </form>
    </div>
  );
};

export default TransactionForm;
