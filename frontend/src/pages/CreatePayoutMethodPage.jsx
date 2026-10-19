import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import PayoutMethodService from "../services/PayoutMethodService";
import PayoutMethodForm from "../components/PayoutMethodForm";
import toast from "react-hot-toast";

const CreatePayoutMethodPage = () => {
  const [types, setTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    PayoutMethodService.getPayoutMethodTypes()
      .then(res => setTypes(res.data))
      .catch(error => {const message = error.response?.data?.message || error.message || "Something went wrong"
            toast.error(message)})
      .finally(() => setLoading(false));
  }, []);

  const handleCreate = async (data) => {
    await PayoutMethodService.createPayoutMethod(data);
    navigate(`/users/${data.userId}`);
  };

  if (loading) return <p>Loading...</p>;

  return (
    <PayoutMethodForm
      payoutTypes={types}
      onSubmit={handleCreate}
      submitLabel="Create"
    />
  );
};

export default CreatePayoutMethodPage;