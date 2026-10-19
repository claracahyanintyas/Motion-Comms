import React, {useState, useContext, useEffect} from 'react'
import { useNavigate } from 'react-router-dom';
import CommissionService from '../services/CommissionService';
import { AuthContext } from '../context/AuthContext';
import toast from 'react-hot-toast';

export const UploadCommission = () => {

    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [downPaymentPercentage, setDownPaymentPercentage] = useState('');
    const {  user, loading } = useContext(AuthContext);
    const navigate = useNavigate();

useEffect(() => {
  if (loading) return;
  if (!user){
    navigate('/login');
  }
}, [loading, user, navigate]);

    const [errors, setErrors] = useState({
      title: '',
      description: '',
      price: '',
      downPaymentPercentage: '',
      commissioneeId: ''
    })

    function saveCommission(e){
      if (loading)
      {
        return;
      }
      if (!user){
        navigate('/login')
        return;
      }

      e.preventDefault();

      console.log(user.id)
      if (validateForm()){
        const commission = { 
          title, 
          description, 
          price: parseFloat(price), 
          downPaymentPercentage: parseFloat(downPaymentPercentage), 
          commissioneeId: user.id 
        };
        console.log(commission);
        
        CommissionService.createCommission(commission)
          .then((response) => {
            navigate(`/commissions/${response.data.id}`);
          })
          .catch((error) => {
            const message =
              error.response?.data?.message ||
              error.message ||
              "Something went wrong";

            toast.error(message);
          });

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
    <div className='text-[#2F4156] flex flex-col justify-center items-center text-center h-full w-full p-4 text-2xl'>
        <form className='flex flex-col items-center justify-center text-left space-y-2 w-1/2'>
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
            <textarea
            className='border-[#C8D9E6] border-2 p-1 rounded-md'
            placeholder='Enter Description..'
            name='description'
            value={description}
            onChange={(e) => setDescription(e.target.value)}>
            </textarea>
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
              onChange={(e) => setPrice(e.target.value)}>
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
              onChange={(e) => setDownPaymentPercentage(e.target.value)}>
              </input>
              { errors.downPaymentPercentage && <div className='text-red-500 text-base'>{errors.downPaymentPercentage}</div>}
            </div>
          </div>

          { errors.commissioneeId && <div className='text-red-500 text-base'>{errors.commissioneeId}</div>}
          <button type="submit" className="bg-[#C8D9E6] mt-2 p-2 rounded-md hover:bg-[#567C8D]" onClick={saveCommission}>Create</button>
        </form>
      </div>

  )
}
