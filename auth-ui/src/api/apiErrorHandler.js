export const handleErrors = async (error) => {
    if(error.response){
        return {
            message: error.response.data.message,
            status: error.response.status,
            details: error.response.data.details || {}
        }
    } else if(error.request){
        return { message: 'No response received from server', status: null};
    } else {
        return {message: error.message, status: null};
    }
}