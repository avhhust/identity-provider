import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const InputField = (props) => {
    const {type, name, value, onChange, onBlur, onFocus, errors, hints, placeholder, required} = props;
    const [toggleBtnText, setToggleBtnText] = useState("show");
    const [isPasswordVisible, setIsPasswordVisible] = useState(false);

    const handleClickShowPassword = (e) => {
        setIsPasswordVisible(prev => !prev);
        setToggleBtnText(isPasswordVisible ? "show" : "hide");
    }

    return (
        <div>
            <div className="input-holder bar">
                {/* {forgotUrl && <Link to={forgotUrl} className='above_link'>forgot {name}?</Link>} */}
                <input
                    type={isPasswordVisible ? "text" : type}
                    name={name}
                    value={value}
                    onChange={onChange}
                    onBlur={onBlur}
                    onFocus={onFocus}
                    required={required}
                />
                <label>{placeholder}</label>
                {
                    type === "password" && <div className="show-password-btn" onClick={handleClickShowPassword}>{toggleBtnText}</div>
                }
            </div>
            <div className="text-underneath">
                <div className="errors">
                    {errors && errors.map(error => <p key={error}>{"*" + error}</p>)}
                </div>
                <div className="hints">
                    {hints && hints.map(hint => <p key={hint}>{"- " + hint}</p>)}
                </div>
            </div>
        </div>
    );
}

export default InputField;