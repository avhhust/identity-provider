import React, { useEffect, useState } from "react";
import NavButton from "../ui/NavButton";
import InputField from "../ui/InputField";
import { useNavigate, useLocation } from "react-router-dom";
import * as authServer from "../../api/apiService.js";
import ResendCode from "../ui/ResendCode.js";

const CodeConfirmationForm = (props) => {
  const [inputData, setInputData] = useState("");
  const [errors, setErrors] = useState({code: []});
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (!location.state || !location.state.email) {
      navigate("/login", { replace: true });
    }
  }, [location, navigate]);

  const validateField = (field) => {
    const errorMessages = [];
    if (!inputData) errorMessages.push("This field is required");
    else if (!inputData.match(/^[A-Z0-9]{8}$/))
      errorMessages.push("Enter a valid code");
    setErrors(prevErrors => ({ ...prevErrors, [field]: errorMessages }));
    return errorMessages.length === 0;
  }

  const isFormValid = () => {
    const field = 'code';
    let isValid = true;
    if (errors[field].length !== 0) {
      isValid = false;
    }
    else if (!validateField(field)) {
      isValid = false;
    }
    return isValid;
  }

  const handleInput = (e) => {
    const { value } = e.target;
    setInputData(prevInputData => prevInputData = value);
  }

  const handleBlur = (e) => {
    validateField(e.target.name);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid()) return;
    setIsLoading(true);

    const otpCode = {
      code: inputData,
      email: location.state.email
    }
    const response = await authServer.post("/api/otp/verify", otpCode);
    if (response.status === 200) {
      navigate(props.forwardUrl, { state: otpCode, replace: true });
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

  return (
    <div className="auth_container">
      <NavButton direction="back" replace />
      <h2>{"Confirmation Code" || props.header}</h2>
      <p style={{ fontSize: "14px" }}>{"Enter the confirmation code we sent to your email address" || props.text}</p>
      <form onSubmit={handleSubmit}>
        <InputField
          type="text"
          name="code"
          value={inputData}
          onChange={handleInput}
          onBlur={handleBlur}
          placeholder="Code"
          errors={errors.code}
        />
        <ResendCode email={location.state.email}/>
        <button className="bar submit_btn" type="submit" disabled={isLoading}>Submit</button>
      </form>
    </div>
  )
}

export default CodeConfirmationForm;