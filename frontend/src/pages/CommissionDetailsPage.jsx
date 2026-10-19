import { useEffect, useState } from 'react'
import { useParams } from "react-router-dom";
import CommissionDetails from '../components/CommissionDetails'
import CommissionService from '../services/CommissionService';
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import toast from 'react-hot-toast';

const CommissionDetailsPage = () => {
  const { id } = useParams(); 
  const [commission, setCommission] = useState(null);
  const {  user  } = useContext(AuthContext);

  let isCommissionee = false;
  let isLoggedIn = false; 

  useEffect(() => {
    CommissionService.getCommission(id)
      .then(res => setCommission(res.data.commission))
      .catch(error => {const message = error.response?.data?.message || error.message || "Something went wrong"
            toast.error(message)});
      }, [id]);

    if (user){
      isLoggedIn = true;
      if (user && commission){
        isCommissionee = user.id === commission.commissionee.id;
      }
      
    }
    if (!commission) return <p>Loading...</p>;

    return(
    <div>
      <CommissionDetails key={commission.id} commission={commission} isCommissionee={isCommissionee} isLoggedIn={isLoggedIn} />
    </div>
    )

      
  
}

export default CommissionDetailsPage