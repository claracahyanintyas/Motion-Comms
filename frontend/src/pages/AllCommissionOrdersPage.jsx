import React, { useEffect, useState, useContext, useCallback  } from 'react';
import CommissionOrderService from '../services/CommissionOrderService';
import CommissionOrdersList from '../components/CommissionOrdersList';
import CommissionOrdersFilter from '../components/CommissionOrdersFilter';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import toast from 'react-hot-toast';

const AllCommissionOrdersPage = () => {
  const [roleFilter, setRoleFilter] = useState(null); 
  const [orders, setOrders] = useState([]);
  const [loadingOrder, setLoadingOrder] = useState(false);
  const {  user, loading } = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
      if (loading) return;
      if (!user){
        navigate('/login');
      }
    }, [loading, user, navigate]);

      /** Fetch orders */
  const fetchOrders = useCallback(async () => {
    if (!user) return;

    setLoadingOrder(true);
    try {
      const response =
        await CommissionOrderService.getAllUserCommission(roleFilter);

      setOrders(response.data.commissionOrders ?? []);
    } catch (error) {
      const message = error.response?.data?.message || error.message || "Something went wrong"
      toast.error(message);
    } finally {
      setLoadingOrder(false);
    }
  }, [user, roleFilter]);

  /** Refetch when filter or user changes */
  useEffect(() => {
    if (!loading && user) {
      fetchOrders();
    }
  }, [fetchOrders, loading, user]);

  return (
    <div className='text-[#2F4156]'>
      <h1 className='text-5xl font-bold p-2'>My Commissions</h1>

      <CommissionOrdersFilter
        currentFilter={roleFilter}
        onFilterChange={setRoleFilter}
      />

      {loadingOrder ? (
        <p>Loading...</p>
      ) : (
        <CommissionOrdersList orders={orders} />
      )}
    </div>
  );
};

export default AllCommissionOrdersPage;
