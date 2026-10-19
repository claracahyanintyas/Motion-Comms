import { useEffect, useState } from "react";
import Commission from "../components/Commission";
import CommissionService from "../services/CommissionService";
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import toast from "react-hot-toast";

export default function AllCommissions() {
  const [commissions, setCommissions] = useState([]);
  const {  user } = useContext(AuthContext);

  useEffect(() => {
    CommissionService.getAllCommissions()
      .then(res => setCommissions(res.data.commissions))
      .catch(error => {const message = error.response?.data?.message || error.message || "Something went wrong"
            toast.error(message)});
  }, []);

  return (
    <div>
      <p className="text-2xl text-[#2F4156]">View the available commissions here!</p>
      <div className="grid grid-cols-3">
        
        {commissions.map(commission => (
          
          <Commission key={commission.id} commission={commission} isLoggedIn={user} isCommissionee={user?.id === commission.commissionee.id}/>
        ))}
      </div>
    </div>

  );
}
