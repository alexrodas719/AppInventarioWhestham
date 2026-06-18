package upn.edu.pe.inventariowh.Red;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    private static Retrofit retrofit=null;
    private static final String URL = "https://westhamapimovil.tryasp.net/api/";

   public static Retrofit getCliente(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
