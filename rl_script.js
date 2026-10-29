
// const now = new Date()
// time = new Date(now.getTime()).toISOString();
// console.log(time.toString());

// const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQcmV0aGFtIiwiaWF0IjoxNzY5MzM0NjM1LCJleHAiOjE3NjkzMzgyMzV9.YFSbwsZfQhEoq-v3ibDoyjjKqLJR0DFt-i6SnsibEeg"
// const email = ["mukulpretham@gmail.com","mukulpreetham462@gmail.com"]

// const payload = {
//     type: "EMAIL",
//     retryCount: 3,
//     scheduledAt: time,
//     from: "schmunna@gmail.com",
//     to: email,
//     subject: "Hello, this is a scheduled email.",
//     body: "This email was scheduled from the client.",
//     contentType: "text/plain"
//   };

// const res = fetch("http://localhost:8080/schedule/email",{method: "POST",
//     headers: {
//       "Content-Type": "application/json",
//       "Authorization": token
//     },
//     body: JSON.stringify(payload)})
//     .then((res) => {
//         if(res.status == 403){
//             return "Error"
//         }
//         return res.json()
//     })
//     .then(content => console.log(content))




async function fun(t){
  return new Promise((resolve,reject)=>{
    setTimeout(()=>{
      console.log(`waited for ${t}`)
      reject("")
    },t)
  })
}

fun(1000).then(()=>{
  console.log("Done")
})
.catch((err)=>{
  console.log("error")
})