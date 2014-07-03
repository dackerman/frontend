(ns frontend.utils.seq)

(defn find-index
  "Finds index of first item in coll that returns truthy for filter-fn"
  [filter-fn coll]
  (first (keep-indexed (fn [i x] (when (filter-fn x) i)) coll)))

(defn select-in
  "Returns a map containing only those entries in map whose keypath is in keypaths
   (select-in {:a {:b 1 :c 2}} [[:a :b]]) => {:a {:b 1}} "
  [map keypathseq]
    (loop [ret {} keypaths (seq keypathseq)]
      (if keypaths
        (let [entry (get-in map (first keypaths))]
          (recur
           (if entry
             (assoc-in ret (first keypaths) entry)
             ret)
           (next keypaths)))
        (with-meta ret (meta map)))))
