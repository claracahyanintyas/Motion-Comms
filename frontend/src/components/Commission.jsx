import React from 'react'
import { Link } from "react-router-dom";
import CommissionOrderService from '../services/CommissionOrderService';
import MediaBox from './MediaBox';
import toast from 'react-hot-toast';
 
  const handleOrder = async (id) => {
    try {
        const res = await CommissionOrderService.createCommissionOrder(id);
        console.log(res)
      }
      catch (error) {
      const message = error.response?.data?.message || error.message || "Something went wrong"
      toast.error(message);
        }
    };
  


export default function Comission({commission, isLoggedIn, isCommissionee}) {
  return (
    <div className="p-5">
      <div className="flex flex-col center shadow-lg text-left py-2 px-4 text-lg">
        <p>@{commission.commissionee.username}</p>
        <Link to={`/commissions/${commission.id}`} className='underline hover:text-[#567C8D]'>Commission {commission.id}: {commission.title}</Link>
        <div className='flex items-center justify-center p-3'>
          <MediaBox url="http://localhost:9100/transaction-proofs/31540f4d-17cd-4fc3-bf05-7b2b28bdab6d"></MediaBox>
        </div>
        
        <h4>Price: {commission.price} eur</h4>
        <p className='line-clamp-2 break-words leading-5 h-10'>{commission.description}</p>
        <Link to={`/commissions/${commission.id}`} className='inline-flex p-2 my-2 self-center bg-[#C8D9E6] hover:bg-[#567C8D] text-center rounded-md'>Order</Link>
      </div>
      {/* {!isCommissionee && isLoggedIn && <button 
            className="mt-2 bg-blue-500 text-white py-1 px-3 rounded hover:bg-blue-600"
            onClick={() => handleOrder(commission.id)}
          >
          Order
        </button>
      } */}

    </div>
  )
}
