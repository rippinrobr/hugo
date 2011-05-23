(ns hugo.ui.views.main-view
  (:import 
     (java.awt.BorderLayout)
     (java.awt.event ActionListener)
     (javax.swing JButton JFrame JPanel JLabel JList JScrollPane DefaultListModel)))

(def act (proxy [ActionListener] []
     (actionPerformed [event] (System/exit 0))))

(defn add-list-items [model items]
  (doseq [item items] 
     (.addElement model item)) )

(defn create-home-view []
   (let  [main-win (JFrame. "@rippinrobr's Hugo Best Novel Database")
    	  panel (JPanel.)
          title-label (JLabel. "The HUGO - Best Novel Nominees and Winners")
          close-button (JButton. "Close")
          list-model (DefaultListModel.)
          list-box (JList. list-model)
          scroll-pane (JScrollPane. list-box)]

   (.add panel title-label java.awt.BorderLayout/PAGE_START)
   (add-list-items list-model ["2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2" "2011 Hugo - Best Novel Nominees and Winner(s)" "       Book 1" "      Book 2"])

   (.setOpaque title-label true)
   (.setBackground title-label (java.awt.Color. 145 220 71))
   (.setForeground title-label java.awt.Color/WHITE)
   (.setFont title-label (java.awt.Font. "Helvetica" java.awt.Font/BOLD, 16))

   (.setBackground close-button (java.awt.Color. 144 180 254))
   (.setForeground close-button java.awt.Color/WHITE)
   (.setFont close-button (java.awt.Font. "Helvetica" java.awt.Font/BOLD, 14))
   (.addActionListener close-button act)

   (.add main-win title-label java.awt.BorderLayout/PAGE_START)
   (.add main-win scroll-pane java.awt.BorderLayout/CENTER)
   (.add main-win close-button java.awt.BorderLayout/PAGE_END)

   (.setSize main-win 400 600)
   (.setVisible main-win true)))
