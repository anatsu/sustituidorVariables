/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables;

import mx.net.alvatroz.sustituidorvariables.view.FrameView;
import javax.swing.JFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author alvaro
 */
@SpringBootApplication
@PropertySource( "classpath:querys.xml")
public class Application {
    
    
    public static void main( String[] args)
    {
        ConfigurableApplicationContext application =  new SpringApplicationBuilder(Application.class).headless(false).run(args);
         
        FrameView frameView = application.getBean( FrameView.class);
        
        
        frameView.setSize(800, 600);
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        frameView.setLocationByPlatform(true);
        frameView.setVisible(true);
        
        
        
    }
}
