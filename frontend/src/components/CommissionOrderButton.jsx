const CommissionOrderButton = ({ orderCommission }) => {
  return (
    <div>
      <button
      className="mt-2"
        onClick={() => orderCommission()}
      >
        Order
      </button>
    </div>
  );
};

export default CommissionOrdersFilter;