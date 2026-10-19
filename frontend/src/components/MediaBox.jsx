import React from "react";

export default function MediaBox({ url }) {
  // simple check based on file extension
  const isVideo = url.match(/\.(mp4|webm|ogg|mov)$/i);

  return (
    <div className="w-4/5 aspect-square bg-gray-100 flex items-center justify-center overflow-hidden rounded-lg">
      {isVideo ? (
        <video
          src={url}
          className="max-w-full max-h-full object-contain"
          muted
          loop
          playsInline
        />
      ) : (
        <img
          src={url}
          alt=""
          className="max-w-full max-h-full object-contain"
        />
      )}
    </div>
  );
}
