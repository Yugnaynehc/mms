(ns mms.controlers.process-queue
  (:require
   [mms.util :as u]
   [mms.models.process-queue :as m]
   [mms.controlers.section-table :as sec]
   [mms.controlers.app :as app]))

(defn get-process-queue
  "获得process-queue解引用后的值"
  []
  (deref m/process-queue))

(defn get-process-queue-key
  "获得process-queue中每一个键值对的键部分，
  即pid的集合"
  []
  (keys (deref m/process-queue)))

(defn get-process-queue-value
  "获得process-queue中每一个键值对的值部分"
  []
  (vals (deref m/process-queue)))

(defn get-current-process-id
  "获得当前进程的id，直接调用controlers.app中的对应函数"
  []
  (app/get-current-process-id))

(defn get-process
  "根据进程id获得进程信息"
  [id]
  (get @m/process-queue id))

(defn add-process
  "将新产生的进程加入进程队列。
  需要提供的参数是内存大小：size，
  以及生命周期：life"
  [size life]
  (let [id (swap! m/process-counter inc)]
      (swap! m/process-queue assoc
             id {:id id :size size
                 :life life :state 0})
      (if (nil? app/get-current-process-id)
        (app/set-current-process-id id))))

(defn input-process
  "根据input框的输入来构造进程"
  [size life]
  (if (u/validate-string-num size life)
    (add-process (js/parseInt size)
                 (js/parseInt life))))

(defn delete-process
  "删除一个进程"
  [id]
  (swap! m/process-queue dissoc id)
  (sec/free-section id))

(defn choose-unloaded-process
  "挑选一个没有载入到内存中的进程"
  []
  (first (filter #(= 0 (:state %)) (get-process-queue-value))))

(defn update-process-by-value
  "更新进程队列中某个进程的状态
  用新值代替旧值"
  [id property value]
  (swap! m/process-queue assoc-in [id property] value))

(defn update-process-by-fn
  "更新进程队列中某个进程的状态
  对旧值执行函数得到新值"
  [id property func]
  (swap! m/process-queue update-in [id property] func))

(defn wake-process
  "唤醒进程"
  [id]
  (update-process-by-value id :state 1))

(defn load-process
  "将进程加载到内存中"
  [process]
  (when-let [section (app/allocate-memory process)]
    (let [{:keys [id size]} process]
      (sec/consume-section id size section)
      (wake-process id))))

(defn suspend-process
  "将进程挂起"
  [id]
  (update-process-by-value id :state 2))

(defn weaken-process
  "减少进程的生命周期"
  [id]
  (update-process-by-fn id :life dec))

