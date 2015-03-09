(ns mms.util)

(defn validate-num
  "检测是否传入的是数字"
  [& xs]
  (every? number? xs))


(defn validate-string-num
  "检测传入的字符串是否表示数字"
  [& xs]
  (every? string? (map #(re-matches #"^\d+$" %) xs))) 
