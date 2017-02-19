
package domain;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package Domain;

/*	Project 
 *  Title  : Web Crawler
 *  Author : More Suhas Rajendra And Mengawade Vaibhav Hanumant
 *  Mobile : +919689649696
 *  Email  : suhasdadamore@gmail.com	
 *  	   : dsuhas4u@gmail.com 	
 *  
 */

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Domain{
    private static Object st1;

	public static void main(String[] args) throws SocketTimeoutException, UnknownHostException, FileNotFoundException, UnsupportedEncodingException, ClassNotFoundException, SQLException, IOException 
	{
PrintWriter writer = new PrintWriter("twww.txt", "UTF-8");
writer.println("The first line");
writer.println("The second line");
writer.println("The second line");
writer.println("The second line");


		String dnm,ch;
		String title,dlnk;
		int lnkcnt=0,dmncnt=0;
		int Meta_Flag=0;
		int Domain_Visit_Count=0,Link_Visit_Count=0;
		Connection con=null;
		InputStreamReader isr=new InputStreamReader(System.in);
		BufferedReader br=new BufferedReader(isr);
		
		LinkedList<String> Domain_Queue=new LinkedList<String>(); 
		LinkedList<String> Link_Queue=new LinkedList<String>(); 
		
		LinkedList<String> DOMAIN_VISITED=new LinkedList<String>(); 
		LinkedList<String> LINK_VISITED=new LinkedList<String>(); 
        
       
			
		System.out.println("\n\n\t\tEnter Seeds To Start Crawling : ");
		
		try
		{
		do
		{
			System.out.print("\t\tEnter Here : ");
			dnm=br.readLine();
			Domain_Queue.add(new String("http://"+dnm));
				
                        System.out.print("\t\tAdd Another Seed(Y/N) : ");
			ch=br.readLine();
					
		}while(!((ch.equals("N")) || (ch.equals("n"))));
				
                    Console console;
                    console = System.console();
		while(!Domain_Queue.isEmpty())
		{
                    String dnm1=new URL(Domain_Queue.poll()).getHost();
                    Link_Queue.add(new String("http://"+dnm1));
					

                    Document docd=Jsoup.connect("http://"+dnm).userAgent("Mozilla Firefox").get();
                    System.out.println("-----------------------------------------------------------------------------------------------------");
                    System.out.println("\t \tVisited Domain :  "+dnm1);
                    System.out.println("-----------------------------------------------------------------------------------------------------");
                    writer.println("visited Domain"+dnm1);
                    
                    dmncnt++;
                    
                    Domain_Visit_Count++;
                    Meta_Flag=0;
					
		// Meta Of Domain 
					
                    Elements metas=null;
                    metas=docd.select("meta");
                    
                    for(Element meta : metas )
                    {
                        
			if(meta.attr("name").equals("keywords")||meta.attr("name").equals("description"))
			{
			//	System.out.println(" \n"+meta.attr("name")+" : "+meta.attr("content"));	
				Meta_Flag=1;
			}
                    }
				
                    
                    if(Meta_Flag==0){	
                        //System.out.println("\n\n-----------------------------------------------------------------------------------------------------");
                       // System.out.println("\t\tNo Meta Found");
                       // System.out.println("-----------------------------------------------------------------------------------------------------");
                    }
					
			
                    DOMAIN_VISITED.add(dnm1);
					
					
                    try
                    {
                        while(!(Link_Queue.isEmpty()))			// Visit All HyperLinks Available On dnm1 ^
			{
						
                            String url=Link_Queue.poll();	 
                            con=Jsoup.connect(url);
					
                            if(con!=null)
                            {
                            //System.out.println("\t @@@Visiting  Link  @@@  :  "+url);
									
                                Document doc=Jsoup.connect(url).timeout(5000).userAgent("Mozilla Firefox").get();
				//System.out.println("***************************************************************************************");
                                //System.out.println("\t\tLink Visited : "+url);			
				//System.out.println("***************************************************************************************");
                                LINK_VISITED.add(url);
				Link_Visit_Count++;
				lnkcnt++;
							 			
			
				// Meta Of Hyperlink page
									
				int lMeta_Flag=0;
				Elements lmetas=null;
				lmetas=doc.select("meta");
									
				for(Element lmeta : lmetas )
				{
									
                                    if(lmeta.attr("name").equals("keywords")||lmeta.attr("name").equals("description"))
                                    {
										
				//	System.out.println("\n"+lmeta.attr("name")+" : "+lmeta.attr("content"));	
					lMeta_Flag=1;
                                    }
				}
                                
										
				if(lMeta_Flag==0){
                         //           System.out.println("\n----------------------------------------------------------------------------------------------------------------");	
                           //         System.out.println("\tNo Meta Found On Sub-Page : "+url);
				//    System.out.println("-----------------------------------------------------------------------------------------------------");						
                                }					
							
									
				//parse url^  And get Hyperlink From page and Added To LinkQueue  
										
				Elements elmnts=doc.select("a");
										
				for(Element elmnt:elmnts)
				{
								
					String lnk=elmnt.attr("abs:href");
											
                                        if(!lnk.equals(""))
					{
												
                                            if(!Link_Queue.contains(lnk))			//Only Unvisited Links Are Added To Queue
                                            {
						lnkcnt++;
                                                //Link_Queue.add(new String(lnk));	
			//			System.out.println("\tFound New Link ["+lnkcnt+"] : "+lnk);
										 
                                            }	
									
									
                                            if(!dnm1.equals(new URL(lnk).getHost()))	// Only Unvisited Domain Names Are Added To Domain Queue
                                            {
										
						if(!DOMAIN_VISITED.contains(new URL(lnk).getHost()))
						{
                                                	dmncnt++;
							Domain_Queue.add(new String("http://"+new URL(lnk).getHost()));
							DOMAIN_VISITED.add(new URL(lnk).getHost());
		//					System.out.println("\n-----------------------------------------------------------------------------------------------------");
                                                        System.out.println("\tFound New Domain : ["+dmncnt+"] : "+new URL(lnk).getHost()+"\n");
                  //                                      System.out.println("-----------------------------------------------------------------------------------------------------");
						writer.println("\nFound New Domain : ["+dmncnt+"] : "+new URL(lnk).getHost()+"\n");
                                                }								
										
                                            }
                                            
									
					}
										
				} //for
									
			}	
						
						
                    }//end of Link Queue while
					
		writer.close();

                    }//end of inner try
				
			
		catch(IOException e)
		{
                    System.out.println("\n\t\t"+e);
		}
				
				
		}	//end String Queue while
				
				
		}	//end of first try
		
		catch(IOException e)
		{
			System.out.println("\n\t\t"+e);
			
		}
		
		
		finally
		{
			System.out.println("\n\t\tTotal Domain Visited : "+Domain_Visit_Count+"\n\t\tTotal Link Visited   : "+Link_Visit_Count+"\n\n\t\tToltal Domain Crawled / New Web Site Found : "+dmncnt+"\n\t\tTotal Hyper Links Crawled/Found            : "+lnkcnt);
		}

	}	//main

}	//class
