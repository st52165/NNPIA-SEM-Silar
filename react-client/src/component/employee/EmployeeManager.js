import React, { useLayoutEffect, useState } from "react";
import { useAuth } from "../AuthContext";
import EmployeeList from "./EmployeeList";
import EmployeeForm from "./EmployeeForm";

function EmployeeManager(props) {

  const [data, setData] = useState([])
  const [isPending, setIsPending] = useState(true)
  const [error, setError] = useState();
  const { token } = useAuth();
  const [updateTimestamp, setUpdateTimestamp] = useState(new Date())
  const [view, setView] = useState("list")

  useLayoutEffect(() => {
    console.log(token)
    fetch(`${process.env.REACT_APP_BASE_URI}/employees`, {
      headers: new Headers({
        'Authorization': 'Bearer ' + token,
      }),
    })
      .then(response => {
        if (response.ok) {
          return response.json()
        }
        throw new Error(`Unable to get data: ${response.statusText}`)
      })
      .then(json => setData(json))
      .catch((err) => setError(err.message))
      .finally(() => setIsPending(false))

  }, [updateTimestamp])

  const deleteItem = (item) => {
    setIsPending(true)
    fetch(`${process.env.REACT_APP_BASE_URI}/employees/${item.empId}`,
      {
        method: 'DELETE',
        headers: new Headers({
          'Authorization': 'Bearer ' + token,
        }),
      })
      .then(response => {
        if (response.ok) {
          return response.json()
        }
        throw new Error(`Unable to delete data: ${response.statusText}`)
      })
      .catch((err) => {
        setError(err.message)
      })
      .finally(() => {
        setUpdateTimestamp(new Date())
        setIsPending(false)
      })
  }

  const onAddItem = (item) => {
    setUpdateTimestamp(new Date())
  }

  return (

    <div>

      <ul>
        <li>
          <button onClick={()=>setView("list")}>List</button>
        </li>
        <li>
          <button onClick={()=>setView("form")}>Form</button>
        </li>
      </ul>

      {isPending && "Loading data..."}
      {error}
      {view==="list" && data && <EmployeeList data={data} onDeleteItem={deleteItem}/>}
      {view==="form" && data && <EmployeeForm onAddItem={onAddItem}/>}
    </div>
  );
}

export default EmployeeManager;