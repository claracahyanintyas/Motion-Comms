import MediaBox from './MediaBox';

export default function ComissionDetails({ user }) {

  return (
    <div className="flex items-center p-4 w-full text-[#2F4156] ">
            <div className='flex'>
                <div className='flex flex-col items-center p-3 w-36'>
                <MediaBox url="http://localhost:9100/transaction-proofs/31540f4d-17cd-4fc3-bf05-7b2b28bdab6d" className='rounded-full'></MediaBox>
                <h1 className='text-2xl'>@{user.username}</h1>
                </div>
                
            </div>
    </div>
  );
}
