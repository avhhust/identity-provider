import React, { useState, useEffect } from "react";
import NavButton from "../ui/NavButton";
import InputField from "../ui/InputField";
import { useLocation, useNavigate } from "react-router-dom";
import * as authServer from "../../api/apiService";

const ResetPasswordForm = () => {
  const [inputData, setInputData] = useState({
    password: "", confirmPassword: ""
  });
  const [errors, setErrors] = useState({
    password: [], confirmPassword: []
  });
  const [isLoading, setIsLoading] = useState(false);

  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    if(!location.state || !location.state.email || !location.state.code){
      navigate("/login", {replace: true});
    }
  }, [location, navigate]);

  const validateField = (field) => {
    const value = inputData[field];
    const errorMessages = [];
    if (!value) {
      errorMessages.push("This field is required");
    } else if (field === "password") {
      if (value.length < 8) {
        errorMessages.push("Shoud contain at least 8 symbols");
      }
      if (!value.match(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+/)) {
        errorMessages.push("Password Should contain at least 1 capital letter and 1 number")
      }
    } else if (field === "confirmPassword") {
      if(value !== inputData.password){
        errorMessages.push("Passwords do not match")
      }
    }
    setErrors(prevErrors => ({ ...prevErrors, [field]: errorMessages }));
    return errorMessages.length === 0;
  }

  const isFormValid = () => {
    let isValid = true;
    for (const field in inputData) {
      if (errors[field].length !== 0) {
        isValid = false;
      }
      else if (!validateField(field)) {
        isValid = false;
      }
    }
    return isValid;
  }

  const handleInput = (e) => {
    const { name, value } = e.target;
    setInputData(
      prevInputData => ({ ...prevInputData, [name]: value })
    );
  }

  const handleBlur = ({ target }) => {
    validateField(target.name);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid()) return;
    setIsLoading(true);

    const resetPasswordDto = {
      email: location.state.email,
      code: location.state.code,
      password: inputData.password
    }

    const response = await authServer.post("/api/auth/reset", resetPasswordDto);
    if(response.status === 200){
      navigate("/login", {replace: true})
    }
    setIsLoading(false);
  }

  return (
    <div className="auth_container">
      <NavButton direction="back" url="/login" label="To Login" />
      <h2>Reset Password</h2>
      <p style={{ fontSize: "14px" }}></p>
      <form onSubmit={handleSubmit}>
        <InputField
          type="password"
          name="password"
          value={inputData.password}
          onChange={handleInput}
          onBlur={handleBlur}
          placeholder="New password"
          errors={errors.password}
        />
        <InputField
          type="password"
          name="confirmPassword"
          value={inputData.confirmPassword}
          onChange={handleInput}
          onBlur={handleBlur}
          placeholder="Confirm new password"
          errors={errors.confirmPassword}
        />
        <button className="bar submit_btn" type="submit" disabled={isLoading}>Submit</button>
      </form>
    </div>
  )
}

export default ResetPasswordForm;