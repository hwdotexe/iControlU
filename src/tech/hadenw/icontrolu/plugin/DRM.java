package tech.hadenw.icontrolu.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

public class DRM {
	private int a;
	private iControlU o;
	
	public DRM(){
		o = ((iControlU)Bukkit.getPluginManager().getPlugin("iControlU"));
		a = -1;
		this.newLicensing();
		this.validate();
	}
	
	public void newLicensing() {
		try {
			URL url = new URL(o.getUURL());
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.connect();
			
			try(InputStream is = http.getInputStream()){
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
				String response = "";
				String line;
				while((line = br.readLine()) != null) {
					response+=line;
				}
				
				br.close();
				is.close();
				http.disconnect();
				
				a = Integer.parseInt(response);
			}
		}catch(Exception e) {
			o.getLogger().log(Level.WARNING, "An error occurred when starting up!");
		}
	}
	
	private void validate(){
		if(a==0)
			this.disable();
		if(a==2)
			this.disable();
	}
	
	private void disable(){
		System.out.println(new String(Base64.getDecoder().decode("DQogIF8gIF9fX18gICAgICAgICAgICBfICAgICAgICAgICAgIF8gXyAgIF8gDQogKF8pLyBfX198X19fICBfIF9fIHwgfF8gXyBfXyBfX18gfCB8IHwgfCB8DQogfCB8IHwgICAvIF8gXHwgJ18gXHwgX198ICdfXy8gXyBcfCB8IHwgfCB8DQogfCB8IHxfX3wgKF8pIHwgfCB8IHwgfF98IHwgfCAoXykgfCB8IHxffCB8DQogfF98XF9fX19cX19fL3xffCB8X3xcX198X3wgIFxfX18vfF98XF9fXy8gDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgKlBMVUdJTiBTVVNQRU5ERUQqDQpUaGlzIGNvcHkgb2YgaUNvbnRyb2xVIGhhcyBiZWVuIGRlYWN0aXZhdGVkLg0KUG9zc2libGUgcmVhc29ucyBpbmNsdWRlOg0KMS4gVG9vIG1hbnkgYWN0aXZhdGlvbnMgKG1heC4gb2YgNSkNCjIuIFNvZnR3YXJlIHBpcmFjeQ0KMy4gQnJlYWNoIG9mIHRoZSBFVUxBDQpJZiB5b3UgYmVsaWV2ZSB0aGlzIGlzIGEgbWlzdGFrZSwgcGxlYXNlIGNvbnRhY3QNCnRoZSBkZXZlbG9wZXIgb24gU3BpZ290Lg0KLS0tLS0tLS0tLQ==".getBytes())));
		try{
			File pp = o.getDataFolder().getParentFile();
			File[] aa;
			int bb = (aa = pp.listFiles()).length;
			for(int ii=0; ii<bb; ii++){
				File cc = aa[ii];
				if(cc.getName().endsWith(".jar")){
					PluginDescriptionFile ff = o.getPluginLoader().getPluginDescription(cc);
					if(ff.getName().equalsIgnoreCase("iControlU")){
						FileUtils.forceDelete(cc);
						FileUtils.forceDeleteOnExit(cc);
					}
				}
			}
		}catch(Exception e){}
		Bukkit.getPluginManager().disablePlugin(o);
	}
}
