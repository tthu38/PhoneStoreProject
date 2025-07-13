package listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.HashSet;

@WebListener
public class ProductViewListener implements ServletRequestListener {
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        HttpSession session = request.getSession();
        HashSet<Integer> productViewed = (HashSet<Integer>) session.getAttribute("productViewed");
        Integer productID;
        if(request.getParameter("productId") != null) {
            try{
                productID = Integer.parseInt(request.getParameter("productId"));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid product ID");
            }
        } else {
            return;
        }
        if(productViewed == null){
            productViewed = new HashSet<>();
            session.setAttribute("productViewed", productViewed);
        }

        HashMap<Integer, Integer> productViewCount = (HashMap<Integer, Integer>) event.getServletContext().getAttribute("productViewCount");
        if(productViewCount == null) {
            productViewCount = new HashMap<>();
            event.getServletContext().setAttribute("productViewCount", productViewCount);
        }

        if(!productViewed.contains(productID)){
            productViewCount.put(productID, productViewCount.getOrDefault(productID, 0) + 1);
            productViewed.add(productID);
        }
    }
}
