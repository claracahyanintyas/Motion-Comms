import { useEffect, useState } from 'react'
import { useParams } from "react-router-dom";
import UserService from '../services/UserService';
import PayoutMethodService from '../services/PayoutMethodService';
import PayoutMethod from '../components/PayoutMethod';
import { useNavigate } from "react-router-dom";
import toast from 'react-hot-toast';
import UserDetails from '../components/UserDetails'

const UserDetailsPage = () => {
  const { id } = useParams(); 
  const [user, setUser] = useState(null);
    const [payoutMethods, setPayoutMethods] = useState([]);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
  
    const fetchMethods = async (id) => {
      setLoading(true);
      try {
        console.log(id)
        const response = await PayoutMethodService.getActivePayoutMethodsByUser(id);
        setPayoutMethods(response.data.payoutMethods);
      } catch (error) {
        const message = error.response?.data?.message || error.message || "Something went wrong"
        toast.error(message);
      }
      setLoading(false);
    };

      useEffect(() => {
        fetchMethods(id);
      }, [id]);

  useEffect(() => {
    UserService.getUser(id)
      .then(res => setUser(res.data.user))
      .catch(console.error);
      }, [id]);

    if (!user) return <p>Loading...</p>;

    return(
    <div> 
      <div>
        <UserDetails user={user}></UserDetails>
      </div>
      <div>
        {payoutMethods.map(payoutMethod => (
          <PayoutMethod key={payoutMethod.id} payoutMethod={payoutMethod} />
        ))}
      </div>
      <div>
        <button
          onClick={() => navigate("/payout-methods/create")}
          className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]"
        >
          Add payment method
        </button>
      </div>
    </div>
    )

      
  
}

export default UserDetailsPage