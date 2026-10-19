import React from 'react'
import { Link } from 'react-router-dom'

export default function HomePage() {
  return (
    <div>
      <h1 className='text-[#2F4156] text-5xl font-bold p-4'>
        Welcome to Motion Comms
      </h1>
      <div className='w-full grid grid-cols-2 m-5'>
        <div className='flex flex-col'>
          <p className='text-2xl'>Browse through the available commission here</p>
          <Link to='/commissions' className='text-xl inline-flex p-2 my-2 self-center bg-[#C8D9E6] hover:bg-[#567C8D] text-center rounded-md'>Commissions</Link>
        </div>
        <div className='flex flex-col'>
          <p className='text-2xl'>Create your own commission</p>
          <Link to='/upload-commission' className='text-xl inline-flex p-2 my-2 self-center bg-[#C8D9E6] hover:bg-[#567C8D] text-center rounded-md'>Upload</Link>
        </div>
      </div>
      </div>
  )
}
