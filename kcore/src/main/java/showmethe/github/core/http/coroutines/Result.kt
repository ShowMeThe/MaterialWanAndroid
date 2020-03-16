package showmethe.github.core.http.coroutines

class Result<T>(var status:String, var response: T? = null,code:Int = -1,var message:String = ""){

    companion object{

        const val Loading = "Loading"
        const val Success = "Success"
        const val Failure = "Failure"
        const val OutTime = "OutTime"

        fun   newLoading() = Result(Loading,null)
        fun  <T>newSuccess(response: T? = null) = Result(Success,response)
        fun  <T>newFailed(response: T? = null) = Result(Failure,response)
        fun  <T>newValue( status:String,response: T? = null) = Result(status,response)

    }

}