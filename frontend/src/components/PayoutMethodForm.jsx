import { useState, useEffect } from "react";
import { useContext } from "react";
import { AuthContext } from '../context/AuthContext';

const PayoutMethodForm = ({
  initialValues = {},
  payoutTypes = [],
  onSubmit,
  submitLabel = "Save"
}) => {

  const [type, setType] = useState(initialValues.type || "");
  const [providerName, setProviderName] = useState(initialValues.providerName || "");
  const [accountName, setAccountName] = useState(initialValues.accountName || "");
  const [accountNumber, setAccountNumber] = useState(initialValues.accountNumber || "");
  const {  user } = useContext(AuthContext);

  const [errors, setErrors] = useState({
    type: "",
    providerName: "",
    accountName: "",
    accountNumber: ""
  });

  function validateForm() {
    let valid = true;
    const errorsCopy = { ...errors };

    if (type) errorsCopy.type = "";
    else {
      errorsCopy.type = "Payout type is required";
      valid = false;
    }

    if (providerName.trim()) errorsCopy.providerName = "";
    else {
      errorsCopy.providerName = "Provider is required";
      valid = false;
    }

    if (accountName.trim()) errorsCopy.accountName = "";
    else {
      errorsCopy.accountName = "Account name is required";
      valid = false;
    }

    if (accountNumber.trim()) errorsCopy.accountNumber = "";
    else {
      errorsCopy.accountNumber = "Account number is required";
      valid = false;
    }

    setErrors(errorsCopy);
    return valid;
  }

  function handleSubmit(e) {
    e.preventDefault();
    if (!validateForm()) return;
    const userId = user.id;

    onSubmit({
      type,
      providerName,
      accountName,
      accountNumber,
      userId
    });
  }

  return (
    <div className="text-[#2F4156] flex flex-col justify-center items-center text-center h-full w-full p-4 text-2xl">
      <form className="flex flex-col items-center justify-center text-left space-y-2 w-1/2">

        {/* Type */}
        <div className="flex flex-col w-full">
          <label className="py-1">Payout Type:</label>
          <select
            className="border-[#C8D9E6] border-2 p-1 rounded-md"
            value={type}
            onChange={(e) => setType(e.target.value)}
          >
            <option value="">Select type...</option>
            {payoutTypes.map(t => (
              <option key={t} value={t}>
                {t.replace("_", " ")}
              </option>
            ))}
          </select>
          {errors.type && <div className="text-red-500 text-base">{errors.type}</div>}
        </div>

        {/* Provider */}
        <div className="flex flex-col w-full">
          <label className="py-1">Provider:</label>
          <input
            className="border-[#C8D9E6] border-2 p-1 rounded-md"
            type="text"
            placeholder="e.g. BPI, GCash"
            value={providerName}
            name='providerName'
            onChange={(e) => setProviderName(e.target.value)}
          />
          {errors.providerName && (
            <div className="text-red-500 text-base">{errors.providerName}</div>
          )}
        </div>

        {/* Account Name */}
        <div className="flex flex-col w-full">
          <label className="py-1">Account Name:</label>
          <input
            className="border-[#C8D9E6] border-2 p-1 rounded-md"
            type="text"
            placeholder="Account holder name"
            value={accountName}
            name='accountName'
            onChange={(e) => setAccountName(e.target.value)}
          />
          {errors.accountName && (
            <div className="text-red-500 text-base">{errors.accountName}</div>
          )}
        </div>

        {/* Account Number */}
        <div className="flex flex-col w-full">
          <label className="py-1">Account Number:</label>
          <input
            className="border-[#C8D9E6] border-2 p-1 rounded-md"
            type="text"
            placeholder="Account / wallet number"
            value={accountNumber}
            name="accountNumber"
            onChange={(e) => setAccountNumber(e.target.value)}
          />
          {errors.accountNumber && (
            <div className="text-red-500 text-base">{errors.accountNumber}</div>
          )}
        </div>

        <button
          className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]"
          onClick={handleSubmit}
          type="submit"
        >
          {submitLabel}
        </button>

      </form>
    </div>
  );
};

export default PayoutMethodForm;
