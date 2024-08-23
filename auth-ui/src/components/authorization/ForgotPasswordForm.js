import React, { useState } from "react";
import InputField from "../ui/InputField";
import NavButton from "../ui/NavButton";
import * as authServer from "../../api/apiService";
import { useNavigate } from "react-router-dom";

const ForgotCredentialsForm = () => {
  const [inputData, setInputData] = useState("");
  const [errors, setErrors] = useState({ email: [] });
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const validateField = (field) => {
    const errorMessages = [];
    if (!inputData) errorMessages.push("This field is required");
    else if (!inputData.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/))
      errorMessages.push("Enter a valid email");
    setErrors(prevErrors => ({ ...prevErrors, [field]: errorMessages }));
    return errorMessages.length === 0;
  }

  const isFormValid = () => {
    const field = "email";
    let isValid = true;
    if (errors[field].length !== 0) {
      isValid = false;
    }
    else if (!validateField(field)) {
      isValid = false;
    }
    return isValid;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid()) return;
    setIsLoading(true);
    const response = await authServer.post("/api/otp", { email: inputData });
    if (response.status === 200) {
      navigate("/otp", { state: { email: inputData } });
    } else {
      if (response.details) {
        const { details } = response;
        for (let field in details) {
          setErrors(prevErrors => ({ ...prevErrors, [field]: [details[field]] }))
        }
      }
    }
    setIsLoading(false);
  }

  const handleInput = (e) => {
    const { value } = e.target;
    setInputData(prevInputData => value);
  }

  const handleBlur = (e) => {
    validateField(e.target.name);
  }

  return (
    <div className="auth_container">
      <NavButton direction="back" />
      <h2>Can"t log in?</h2>
      <p style={{ fontSize: "14px" }}>Enter the email address associated with your account and we will send you the code to reset your password</p>
      <form onSubmit={handleSubmit}>
        <InputField
          type="text"
          name="email"
          value={inputData}
          onChange={handleInput}
          onBlur={handleBlur}
          placeholder="Email"
          errors={errors.email}
        />
        <button className="bar submit_btn" type="submit" disabled={isLoading}>Next</button>
      </form>
    </div>
  )
}
export default ForgotCredentialsForm;