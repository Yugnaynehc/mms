(ns mms.controlers.dashboard
  (:require
   [mms.controlers.app :as app]
   [mms.controlers.process-queue :as pro]
   [mms.controlers.section-table :as sec]))


(defn next-model-state
  "计算下一个状态"
  []
  (let [current-pid (app/get-current-process-id)
        current-process (pro/get-process current-pid)]
    ;; 如果存在当前进程
    (when-not (nil? current-pid)
      ;; 减少当前进程的生命周期
      (pro/weaken-process current-pid)
      ;; 如果当前进程生命周期耗尽
      ;; 要注意的是，waken-process并不会修改current-process的值，
      ;; 所以当current-process的周期为1时，就应该将其移除了
      (if (= 1 (:life current-process))
        (do
          ;; 将当前进程删除
          (pro/delete-process current-pid)
          ;; 清空当前进程记录
          (app/set-current-process-id nil))
        ;; 否则，将当前进程挂起
        (pro/suspend-process current-pid))))

  (let [next-pid (app/get-next-process-id)
        next-process (pro/get-process next-pid)]
    ;; 如果存在下一个进程
    
    (when-not (nil? next-pid)
      ;; 依据下一个进程的状态来判断操作
      (case (:state next-process)
        ;; 如果下一个进程还未载入内存中，则将其载入内存
        0 (pro/load-process next-process)
        ;; 如果下一个进程仍是自己
        1 nil
        ;; 如果下一个进程处在挂起状态，则将其唤醒
        2 (pro/wake-process next-pid))
      
      ;; 变更当前进程
      (app/set-current-process-id next-pid))))


(defn clean-model-state
  "清除模型数据"
  []
  (pro/clean-process-queue)
  (sec/clean-section-table))


(defn generate-random-memory
  "产生一块随机内存，大小为8的倍数"
  [min max]
  (let [raw (+ min (rand (- max min)))
        size (* 8 (quot raw 8))]
    (sec/add-memory size)))

(defn generate-random-process
  "产生(0, n]个随机进程，
  每个进程的大小范围为(0, size],
  生命周期范围为(0, life]"
  [n size life]
  (let [num (inc (rand-int n))]
    (doseq [id (range num)]
      (pro/add-process (inc (rand-int size))
                       (inc (rand-int life))))))
