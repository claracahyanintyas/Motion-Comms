import React, {useState} from 'react';
import Modal from 'react-modal';
import { AuthContext } from '../context/AuthContext';
import { useContext } from 'react';
import CommissionService from '../services/CommissionService';
import toast from 'react-hot-toast';

function EditCommissionModal({ isOpen, commission, onClose, onUpdate }) {
    const [title, setTitle] = useState(commission.title);
    const [description, setDescription] = useState(commission.description);
    const [price, setPrice] = useState(commission.price);
    const [downPaymentPercentage, setDownPaymentPercentage] = useState(commission.downPaymentPercentage * 100);
    const {  user } = useContext(AuthContext);
        const [errors, setErrors] = useState({
          title: '',
          description: '',
          price: '',
          downPaymentPercentage: '',
          commissioneeId: ''
        })

    function updateCommission(e){
      if (!user){
        navigate('/login')
        return;
      }

      e.preventDefault();

      console.log(user.id)
      if (validateForm()){
        const updatedCommission = { title, description, price, downPaymentPercentage};
        console.log(updatedCommission);
        const id = commission.id;
          CommissionService.updateCommission(id, updatedCommission)
          .then(() => {toast.success("Successfully updated the data")
            onClose();
            onUpdate({
            ...commission,
            title,
            description,
            price,
            downPaymentPercentage: Number(downPaymentPercentage) / 100
            });
          } )          
          .catch((error) => {
            const message =
              error.response?.data?.message ||
              error.message ||
              "Something went wrong";

            toast.error(message);
          })

      }
    }


    function validateForm(){
      let valid = true;
      const errorsCopy = {... errors}

      if (title.trim()){
        errorsCopy.title = '';
      } else {
        errorsCopy.title = 'title is required';
        valid = false;
      }

      if (price > 0){
        errorsCopy.price = '';
      } else {
        errorsCopy.price = 'price needs to be above 0';
        valid = false;
      }
      if (downPaymentPercentage > 0){
        errorsCopy.downPaymentPercentage = '';
      } else {
        errorsCopy.downPaymentPercentage = 'down payment percentage needs to be above 0';
        valid = false;
      }

      setErrors(errorsCopy);
      return valid;
    }
  return (
    <div className='w-1/2'>
        <Modal
        isOpen={isOpen}
        onRequestClose={onClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
        ariaHideApp={false}
        >
        <form className='flex flex-col items-center justify-center text-left space-y-2 w-full'>
          <div className='flex flex-col w-full'>
            <label className='py-1'>Title:</label>
            <input
            className='border-[#C8D9E6] border-2 p-1 rounded-md'
            type='text'
            placeholder='Enter Title..'
            name='title'
            value={title}
            onChange={(e) => setTitle(e.target.value) }>
            </input>
            { errors.title && <div className='text-red-500 text-base'>{errors.title}</div>}
          </div>
          <div className='flex flex-col w-full'>
            <label className='py-1'>Description:</label>
            <input
            className='border-[#C8D9E6] border-2 p-1 rounded-md'
            type='text'
            placeholder='Enter Description..'
            name='description'
            value={description}
            onChange={(e) => setDescription(e.target.value)}>
            </input>
          </div>
          <div className='grid grid-cols-2 gap-4 w-full'>
            <div className='flex flex-col'>
              <label className='py-1'>Price:</label>
              <input
              className='border-[#C8D9E6] border-2 p-1 rounded-md'
              type='number'
              placeholder='Enter Price..'
              name='price'
              value={price}
              step={0.01}
              onChange={(e) => setPrice(Number(e.target.value))}>
              </input>
              { errors.price && <div className='text-red-500 text-base'>{errors.price}</div>}
            </div>
            <div className='flex flex-col'>
              <label className='py-1'>Down Payment Percentage:</label>
              <input
              className='border-[#C8D9E6] border-2 p-1 rounded-md'
              type='number'
              placeholder='Enter DP percentage..'
              name='downPaymentPercentage'
              value={downPaymentPercentage}
              step={0.01}
              onChange={(e) => setDownPaymentPercentage(Number(e.target.value))}>
              </input>
              { errors.downPaymentPercentage && <div className='text-red-500 text-base'>{errors.downPaymentPercentage}</div>}
            </div>
          </div>

          { errors.commissioneeId && <div className='text-red-500 text-base'>{errors.commissioneeId}</div>}
          <div className='flex gap-4'>
            <button className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]" type='submit' onClick={updateCommission}>Save</button>
            <button className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]" onClick={onClose}>Close</button>
          </div>

        </form>
        </Modal>
    </div>
  )
}

export default EditCommissionModal