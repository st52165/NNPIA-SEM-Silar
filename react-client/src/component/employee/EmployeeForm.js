import React, { useLayoutEffect, useState } from "react";
import { useAuth } from "../AuthContext";

function EmployeeForm({ onAddItem }) {

  const [isError, setIsError] = useState(false);
  const [data, setData] = useState({});
  const { token } = useAuth();

  const handleInputChange = (event) => {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;
    data[name] = value
    setData({ ...data })
  }

  const addItem = (e) => {
    e.preventDefault()
    fetch(`${process.env.REACT_APP_BASE_URI}/employees`,
      {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
        body: JSON.stringify(data) // body data type must match "Content-Type" header
      })
      .then(response => {
        if (response.ok) {
          return response.json()
        }
        throw new Error(`Unable to add data: ${response.statusText}`)
      })
      .then((item) => {
        onAddItem(item);
        return item;
      })
      .then((item)=> alert(JSON.stringify(item)))
      .catch((err) => {
        setIsError(err.message)
      })

  }

  return (
    <form onSubmit={addItem}>

      <div>
        <input placeholder={"name"} type={"text"} name={"name"} onChange={handleInputChange}/>
      </div>
      <div>
        <input placeholder={"designation"} type={"text"} name={"designation"} onChange={handleInputChange}/>
      </div>

      <button>Add employee</button>
      {isError}
      <div style={{backgroundColor: "yellow", padding: "10px", margin: "10px"}}>
      You have to logged as <b>javainuseWithRole</b> user with <b>password</b>
      </div>
    </form>
  )
}

export default EmployeeForm;