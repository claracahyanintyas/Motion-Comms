import { Link } from "react-router-dom";
 
export default function PayoutMethod({payoutMethod}) {
  return (
    <div className="p-5">
      <div className="flex flex-col center bg-gray-200 text-center p-2">
        <h4>Payout method: {payoutMethod.providerName}</h4>
        <h4>Type: {payoutMethod.type}</h4>
      </div>
    </div>
  )
}
