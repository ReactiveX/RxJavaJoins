/**
 * Copyright 2014 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rx.joins;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action9;
import rx.functions.Func9;

/**
 * Represents an execution plan for join patterns.
 */
public final class Plan9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends Plan0<R> {
    protected final Pattern9<T1, T2, T3, T4, T5, T6, T7, T8, T9> expression;
    protected final Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> selector;

    public Plan9(Pattern9<T1, T2, T3, T4, T5, T6, T7, T8, T9> expression, Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> selector) {
        this.expression = expression;
        this.selector = selector;
    }

    @Override
    public ActivePlan0 activate(Map<Object, JoinObserver> externalSubscriptions,
            final Observer<R> observer, final Action1<ActivePlan0> deactivate) {
        Action1<Throwable> onError = onErrorFrom(observer);

        final JoinObserver1<T1> jo1 = createObserver(externalSubscriptions, expression.o1(), onError);
        final JoinObserver1<T2> jo2 = createObserver(externalSubscriptions, expression.o2(), onError);
        final JoinObserver1<T3> jo3 = createObserver(externalSubscriptions, expression.o3(), onError);
        final JoinObserver1<T4> jo4 = createObserver(externalSubscriptions, expression.o4(), onError);
        final JoinObserver1<T5> jo5 = createObserver(externalSubscriptions, expression.o5(), onError);
        final JoinObserver1<T6> jo6 = createObserver(externalSubscriptions, expression.o6(), onError);
        final JoinObserver1<T7> jo7 = createObserver(externalSubscriptions, expression.o7(), onError);
        final JoinObserver1<T8> jo8 = createObserver(externalSubscriptions, expression.o8(), onError);
        final JoinObserver1<T9> jo9 = createObserver(externalSubscriptions, expression.o9(), onError);

        final AtomicReference<ActivePlan0> self = new AtomicReference<ActivePlan0>();

        ActivePlan0 activePlan = new ActivePlan9<T1, T2, T3, T4, T5, T6, T7, T8, T9>(
        		jo1, jo2, jo3, jo4, jo5, jo6, jo7, jo8, jo9,
                new Action9<T1, T2, T3, T4, T5, T6, T7, T8, T9>() {
                    @Override
                    public void call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
                        R result;
                        try {
                            result = selector.call(t1, t2, t3, t4, t5, t6, t7, t8, t9);
                        } catch (Throwable t) {
                            observer.onError(t);
                            return;
                        }
                        observer.onNext(result);
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                    	ActivePlan0 ap = self.get();
                        jo1.removeActivePlan(ap);
                        jo2.removeActivePlan(ap);
                        jo3.removeActivePlan(ap);
                        jo4.removeActivePlan(ap);
                        jo5.removeActivePlan(ap);
                        jo6.removeActivePlan(ap);
                        jo7.removeActivePlan(ap);
                        jo8.removeActivePlan(ap);
                        jo9.removeActivePlan(ap);
                        deactivate.call(ap);
                    }
                });

        self.set(activePlan);

        jo1.addActivePlan(activePlan);
        jo2.addActivePlan(activePlan);
        jo3.addActivePlan(activePlan);
        jo4.addActivePlan(activePlan);
        jo5.addActivePlan(activePlan);
        jo6.addActivePlan(activePlan);
        jo7.addActivePlan(activePlan);
        jo8.addActivePlan(activePlan);
        jo9.addActivePlan(activePlan);

        return activePlan;
    }

}
