package integration.interceptors;

import integration.entities.Requests;
import integration.repository.RequestsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Component
public class Interceptor implements HandlerInterceptor {

    @Autowired
    RequestsRepository requestsRepository;


    private String token;
    private String method;
    private boolean freeToGo=true;
    private List<Requests> list;
    private String adminResponse;
    private boolean refused=false;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Requests requestToHandle=new Requests();
        method =request.getMethod();
        String token0=request.getHeader("Authorization");
        token=token0.substring(7,token0.length());
        requestToHandle.setToken(token);
        requestToHandle.setRequestType(method);
        requestToHandle.setAuthorised(false);

        if(method.equals("DELETE") || method.equals("PUT")){
        requestsRepository.save(requestToHandle);
        freeToGo=false;
            System.out.println(method+" request :do you approve it?");
            Scanner scanner=new Scanner(System.in);
            adminResponse=scanner.nextLine();
            do {
                list = requestsRepository.findAllByOrderByIdAsc();
                if(adminResponse.equals("y")) {
                    Requests requestSet = new Requests();
                    requestSet = list.get(list.size() - 1);
                    requestSet.setAuthorised(true);
                    requestsRepository.save(requestSet);
                }else{
                    refused=true;
                }
            } while (list.get(list.size() - 1).isAuthorised()==false && refused==false);
        }else{
            freeToGo=true;
        }
        if(refused==true){
            response.sendError(401);
            freeToGo=true;
            refused=false;
        }
        return freeToGo;
    }


}