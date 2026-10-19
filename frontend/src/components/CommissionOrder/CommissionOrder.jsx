import React from 'react'
import { formatDateTime } from '../../utils/date'
import MediaBox from '../MediaBox'
import { Link } from 'react-router-dom'

function CommissionOrder({order}) {
  return (
        <div
  className="
    relative group
    rounded-md p-4
    shadow-md
    transition
    hover:shadow-xl hover:-translate-y-0.5
    bg-white
  "
>
  <h1 className="text-xl font-semibold group-hover:text-[#567C8D] transition-colors">
    Order {order.id}: {order.commission.title}
  </h1>

  <div className="flex justify-center p-4 w-1/2 mx-auto">
    <MediaBox url="http://localhost:9100/transaction-proofs/31540f4d-17cd-4fc3-bf05-7b2b28bdab6d" />
  </div>

  <p>Started At: {formatDateTime(order.createdAt)}</p>
  <p>Seller: {order.seller.username}</p>
  <p>Buyer: {order.buyer.username}</p>
  <p>Status: {order.status}</p>

  <Link
    to={`/commission-orders/${order.id}`}
    className="absolute inset-0 z-10"
    aria-label={`View order ${order.id}`}
  />
</div>


  )
}

export default CommissionOrder