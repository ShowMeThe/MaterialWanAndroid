package showmethe.github.core.http.coroutines

class Result<T>(var status:String, var response: T? = null,var code:Int = -1,var message:String = ""){

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


    private var loading:(()->Unit)? = null
    fun whenLoading(loading:()->Unit){
        this.loading = loading
    }

    private var success:(T?.()->Unit)? = null
    fun whenSuccess(success:(T?.()->Unit)){
        this.success = success
    }

    private var failed:((message:String,code:Int?)->Unit)? = null
    fun whenFailed(failed:(message:String,code:Int?)->Unit){
        this.failed = failed
    }

   private var outTime:(()->Unit)? = null
    fun whenOutTime(outTime:()->Unit){
        this.outTime = outTime
    }


    fun  whenStatus(result: Result<T>.()->Unit){
        result.invoke(this)
        when(status){
            Loading ->{
                loading?.invoke()
            }
           Success->{
                success?.invoke(response)
            }
            Failure ->{
                failed?.invoke(message,code)
            }
            OutTime ->{
                outTime?.invoke()
            }
        }
    }

}

