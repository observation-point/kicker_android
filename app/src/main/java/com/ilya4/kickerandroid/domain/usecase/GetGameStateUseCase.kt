package com.ilya4.kickerandroid.domain.usecase

import com.ilya4.kickerandroid.data.io.model.AsyncData
import com.ilya4.kickerandroid.domain.entity.game.state.GameStateResponse
import com.ilya4.kickerandroid.domain.entity.RestError
import com.ilya4.kickerandroid.domain.executor.PostExecutionThread
import com.ilya4.kickerandroid.domain.executor.ThreadExecutor
import com.ilya4.kickerandroid.presentation.app.RestManager
import io.reactivex.Observable
import javax.inject.Inject

class GetGameStateUseCase @Inject constructor(threadExecutor: ThreadExecutor,
                                              postExecutionThread: PostExecutionThread,
                                              private val restManager: RestManager) :
        UseCase<GetGameStateUseCase.Result, GetGameStateUseCase.Params> (threadExecutor, postExecutionThread) {


    override fun buildUseCaseObservable(params: Params): Observable<Result> {
        return Observable.create{ subscriber ->
            val asyncData = object: AsyncData<GameStateResponse> {
                override fun onSuccess(data: GameStateResponse) {
                    if (!subscriber.isDisposed) {
                        subscriber.onNext(Result(null, data))
                        subscriber.onComplete()
                    }
                }

                override fun onError(restError: RestError) {
                    if (!subscriber.isDisposed) {
                        subscriber.onNext(Result(restError.code, null))
                        subscriber.onComplete()
                    }
                }

                override fun onFailure(t: Throwable) {
                    if (!subscriber.isDisposed) {
                        subscriber.onError(t)
                        subscriber.onComplete()
                    }
                }
            }
            restManager.getGameState(asyncData)
        }
    }

    class Params() {
        companion object {
            fun forGetGameState() : Params = Params()
        }
    }

    open class Result(val errorMessage : String?,
                      val gameStateResponse: GameStateResponse?)
}