const CommissionOrdersFilter = ({ currentFilter, onFilterChange }) => {
  return (
    <div className="flex space-x-4 justify-center text-xl ">
      <div> Filter by: </div>
      <div className="space-x-2 ">
        <button
          className={ currentFilter === null ? 'font-bold text-[#567C8D] ' : 'underline hover:-translate-y-0.5' }
          onClick={() => onFilterChange(null)}
        >
          All
        </button>
        <button
          className={ currentFilter === 'buyer' ? 'font-bold text-[#567C8D]' : 'underline hover:-translate-y-0.5' } 
          onClick={() => onFilterChange('buyer')}
        >
          Buyer
        </button>
        <button
          className={ currentFilter === 'seller' ? 'font-bold text-[#567C8D]' : 'underline hover:-translate-y-0.5' }
          onClick={() => onFilterChange('seller')}
        >
          Seller
        </button>
      </div>

    </div>
  );
};

export default CommissionOrdersFilter;