import CommissionOrder from "./CommissionOrder/CommissionOrder";

const CommissionOrdersList = ({ orders }) => {
  if (!orders || orders.length === 0) {
    return <p>No commissions found.</p>;
  }

  return (
    <div className="grid gap-4 sm:grid-cols-1 lg:grid-cols-3">
      {orders.map(order => (
        <CommissionOrder key={order.id} order={order} />
      ))}
    </div>
  );
};

export default CommissionOrdersList;
