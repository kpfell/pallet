(ns pallet.actions.direct.conditional-test
  (:require pallet.actions.direct.conditional)
  (:use
   clojure.test
   [pallet.build-actions :only [build-actions]]
   [pallet.actions :only [exec-script plan-when plan-when-not]]
   [pallet.common.logging.logutils :only [logging-threshold-fixture]]
   [pallet.node-value :only [assign-node-value make-node-value node-value]]))

(use-fixtures :once (logging-threshold-fixture))

(deftest when-test
  (is (= "c\n"
         (first (build-actions {}
                  (plan-when (= 1 1)
                    (exec-script "c")))))
      "true condition causes when block to run")
  (is (= "\n"
         (first (build-actions {}
                  (plan-when false
                    (exec-script "c")))))
      "non-true condition causes when block not to run")
  (let [nv (make-node-value 'nv)]
    (is (= "c\n"
           (first (build-actions {}
                    (assign-node-value nv true)
                    (plan-when @nv
                      (exec-script "c")))))
        "true node-value causes when block to run")
    (is (= "\n"
           (first (build-actions {}
                    (assign-node-value nv nil)
                    (plan-when @nv
                      (exec-script "c")))))
        "non-true node-value causes when block not to run")))

(deftest when-not-test
  (is (= "c\n"
         (first (build-actions {}
                  (plan-when-not false
                    (exec-script "c")))))
      "false condition causes if block to run")
  (is (= "\n"
         (first (build-actions {}
                  (plan-when-not true
                    (exec-script "c")))))
      "true condition causes if block not to run"))