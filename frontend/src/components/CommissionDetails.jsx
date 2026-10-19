import React, { useState } from 'react';
import EditCommissionModal from './EditCommissionModal';
import CommissionOrderService from '../services/CommissionOrderService';
import MediaBox from './MediaBox';
import { Link } from 'react-router-dom';
import toast from 'react-hot-toast';

export default function ComissionDetails({ commission, isCommissionee, isLoggedIn }) {
  const [showModal, setShowModal] = useState(false);
  const [currentCommission, setCurrentCommission] = useState(commission);

  const handleUpdate = (updatedCommission) => {
    setCurrentCommission(updatedCommission);
  };

    const handleOrder = async (id) => {
      try {
          const res = await CommissionOrderService.createCommissionOrder(id);
          toast.success('Successfully ordered this commission');
          console.log(res)
        }
        catch (error) {
        const message = error.response?.data?.message || error.message || "Something went wrong"
        toast.error(message);
          }
      };
      

  return (
    <div className="flex flex-col items-center justify-center p-4 w-full text-[#2F4156] ">
      <Link to={`/users/${currentCommission.commissionee.id}`} className='text-start bg-[#C8D9E6] w-4/5 p-2 hover:text-[#567C8D] underline'>@{currentCommission.commissionee.username}</Link>
      <div className=" w-4/5 flex flex-col items-center center text-left p-2 shadow-xl ">
      
        <h1>Commission {currentCommission.id}: {currentCommission.title}</h1>
        <div className='flex items-center justify-center p-3'>
          <MediaBox url="http://localhost:9100/transaction-proofs/31540f4d-17cd-4fc3-bf05-7b2b28bdab6d"></MediaBox>
        </div>
        <p>Total Price: {currentCommission.price} eur</p>
        <p>Down Payment: {currentCommission.price*currentCommission.downPaymentPercentage} eur</p>
        <h4>Description</h4>
        <p className='break-words '>{currentCommission.description}</p>
      </div>

            {!isCommissionee && isLoggedIn && <button 
            className="mt-2 py-1 px-3 rounded bg-[#C8D9E6] hover:bg-[#567C8D]"
            onClick={() => handleOrder(commission.id)}
          >
          Order
        </button>
      }

      {isCommissionee && <div className="mt-3 flex justify-center">
        <button 
          className="mt-2 py-1 px-3 rounded bg-[#C8D9E6] hover:bg-[#567C8D]"
          onClick={() => setShowModal(true)}
        >
          Edit Commission
        </button>
      </div>}

      
        {isCommissionee && (
        <EditCommissionModal
          commission={currentCommission}
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          onUpdate={handleUpdate}
        />
      )}
    </div>
  );
}
