import React, {useState, useEffect} from "react";
import * as authServer from "../../api/apiService.js";

const ResendCode = (props) => {
    const [minutes, setMinutes] = useState(1);
    const [seconds, setSeconds] = useState(59);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const interval = setInterval(() => {
            if(seconds > 0){
                setSeconds(seconds - 1);    
            }
            if(seconds === 0){
                if(minutes === 0){
                    clearInterval(interval);
                } else {
                    setSeconds(59);
                    setMinutes(minutes-1);
                }
            }
        }, 1000);
        return () => {
            clearInterval(interval);
        };
    }, [seconds]);

    const resetCounter = () => {
        setMinutes(1);
        setSeconds(59);
    }

    const resendCode = async (e) => {
        e.preventDefault();

        setIsLoading(true);
        resetCounter();
        
        const response = await authServer.post('/api/otp', { email: props.email});

        setIsLoading(false);
    }

    return (
        <div className="two_column_container">
            <p>Time remaining: {" "}
                <span style={{ fontWeight: "bold" }}>
                    {minutes < 10 ? `0${minutes}` : minutes}
                    {":"}
                    {seconds < 10 ? `0${seconds}` : seconds}
                </span>
            </p>
            <button
                className="text_btn"
                onClick={resendCode}
                disabled={isLoading || minutes > 0 || seconds > 0}
            >
                Resend Code
            </button>
        </div>
    )
}

export default ResendCode;