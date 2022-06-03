import React from "react";

function EmployeeList({ data, onDeleteItem }) {

  return (
    <div>
      {data.map(item=> <div key={`item-${item.empId}`}>{JSON.stringify(item)} | <button onClick={()=> onDeleteItem(item)}>x</button></div>)}
    </div>
  );
}

export default EmployeeList;