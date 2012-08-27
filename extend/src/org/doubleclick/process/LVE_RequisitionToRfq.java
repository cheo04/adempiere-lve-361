/* Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2007 e-Evolution,SC. All Rights Reserved.               *
 * Contributor(s): Rafael Tomás Salazar Colmenárez www.dcsla.com    		  *
 *****************************************************************************/
package org.doubleclick.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MInvoiceSchedule;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderPaySchedule;
import org.compiere.model.MRfQ;
import org.compiere.model.MRfQLine;
import org.compiere.model.MRfQLineQty;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;

/**
 *	Venezuela 
 *  LVE_DeletePayment
 *  @author Rafael Tomás Salazar Colmenárez rsalazar@dcsla.com - rtsc08@gmail.com
 *  @version $Id: LVE_DeleteOrCancelInvoice  2011/08/01  $
 */
public class LVE_RequisitionToRfq extends SvrProcess
{
	/**	Org						*/
	private int			p_AD_Org_ID = 0;	
	/** Client                  */
	private int         p_AD_Client_ID=0;
	/**	Requisition		*/
	private int 	    p_M_Requisition_ID = 0;
	/**	RFQ		*/
	private int 	    p_C_Rfq_ID = 0;
	

	
	
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Requisition_ID"))
				p_M_Requisition_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Rfq_ID"))
				p_C_Rfq_ID = para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}	

	/**
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{

		String sql = ""
			+ "SELECT m_product_id, "
			+ "       qty, "
			+ "       description, "
			+ "       line "
			+ "FROM   m_requisitionline "
			+ "WHERE  m_requisition_id = " + p_M_Requisition_ID;


		
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				String sqlInsert= "";
				MRfQ mrfq = new MRfQ(getCtx(),p_C_Rfq_ID, null);
				
				while (rs.next()){
					
//					Copy Lines
					MRfQLine newLine = new MRfQLine ( mrfq);
						newLine.setLine(rs.getInt(4));
						newLine.setDescription(rs.getString(3));
					//	newLine.setHelp(lines[i].getHelp());
						newLine.setM_Product_ID(rs.getInt(1));
					//	newLine.setM_AttributeSetInstance_ID(lines[i].getM_AttributeSetInstance_ID());
					//	newLine.setDateWorkStart();
					//	newLine.setDateWorkComplete();
				//		newLine.setDeliveryDays(lines[i].getDeliveryDays());
						newLine.saveEx();
						//	Copy Qtys
						
							MRfQLineQty newQty = new MRfQLineQty (newLine);
					//		newQty.setC_UOM_ID(qtys[j].getC_UOM_ID());
							newQty.setQty(rs.getBigDecimal(2));
							newQty.setIsOfferQty( true);
					//		newQty.setIsPurchaseQty(qtys[j].isPurchaseQty());
					//		newQty.setMargin(qtys[j].getMargin());
							newQty.saveEx();
						
				
			    }
			   
			    pstmt.close();
			    return  "Proceso completado. "  ;
		}
	
//	
//			String sqlupd = ""
//				+ "UPDATE c_invoice "
//				+ "SET    ispaid='N' "
//				+ "WHERE c_invoice_id in (" + invoices+ ")";
//			
//		    int no1 = DB.executeUpdate(sqlupd, null);
//			log.fine("LVE_DeletePaymente -> c_invoice Update #" + no1);
//			msg+= (no1<0) ?" Error... LVE_DeletePaymente." :"";
//
//			String sqldel= ""
//							+ "DELETE FROM lco_invoicewithholding "
//							+ "WHERE  lco_invoicewithholding_id IN (SELECT lco_invoicewithholding_id "
//							+ "                                     FROM   lve_paymentwithholding "
//							+ "                                     WHERE  c_payment_id = '"+p_C_Payment_ID+"') ";
//
//			 int no2 = DB.executeUpdate(sqldel, null);
//			 log.fine("LVE_DeletePaymente -> lco_invoicewithholding Delete #" + no2);
//			 msg+= (no2<0) ?" Error... LVE_DeletePaymente." :"";
//
//			 
//			 String sqldelFacAcc= ""
//					+ "DELETE FROM fact_acct "
//					+ "WHERE   ad_table_id= '735' and record_id in ( "
//					+ 							"SELECT c_allocationhdr_id "
//					+ 							"FROM   c_allocationline "
//					+ 							"WHERE  c_payment_id =  '"+p_C_Payment_ID+"') ";
//
//			 int no3 = DB.executeUpdate(sqldelFacAcc, null);
//			 log.fine("LVE_DeletePaymente -> fact_acct Delete #" + no3);
//			 msg+= (no3<0) ?" Error... LVE_DeletePaymente." :"";
//
//		
//			 sqldelFacAcc = ""
//				 + "DELETE FROM fact_acct "
//				 + "WHERE  record_id IN (SELECT c_allocationhdr_id "
//				 + "                     FROM   c_allocationline "
//				 + "                     WHERE  c_payment_id = '"+p_C_Payment_ID+"') "
//				 + "       AND ad_table_id = '735' ";
//			 no3 = DB.executeUpdate(sqldelFacAcc, null);
//			 log.fine("LVE_DeletePaymente -> fact_acct Delete #" + no3);
//			 msg+= (no3<0) ?" Error... LVE_DeletePaymente." :"";
//
//			 
//			 String sql3 = ""
//				 + "   SELECT c_allocationhdr_id "
//				 + "   FROM   c_allocationline "
//				 + "    WHERE  c_payment_id = '"+p_C_Payment_ID+"'";
//
//				
//			PreparedStatement pstmt3 = null;
//			ResultSet rs3 = null;
//			pstmt3 = DB.prepareStatement(sql3, null);
//			rs3 = pstmt3.executeQuery();
//			String allocationhdr="";
//			while (rs3.next()){
//				allocationhdr+= "'"+String.valueOf((rs3.getInt(1)))+"',";
//		    }
//			allocationhdr+="'0'";
//		    pstmt3.close();
//		 
//			 sqldel = ""
//				 + "DELETE FROM c_allocationline "
//				 + "WHERE  c_payment_id = '"+p_C_Payment_ID+"' ";
//			 no2 = DB.executeUpdate(sqldel, null);
//			 log.fine("LVE_DeletePaymente -> c_allocationline Delete #" + no2);
//			 msg+= (no2<0) ?" Error... LVE_DeletePaymente." :"";
//
//
//			 sqldel = ""
//				 + "DELETE FROM  c_allocationhdr "
//				 + "WHERE  c_allocationhdr_id IN ("+allocationhdr+") ";
//
//			 no2 = DB.executeUpdate(sqldel, null);
//			 log.fine("LVE_DeletePaymente -> c_allocationhdr Delete #" + no2);
//			 msg+= (no2<0) ?" Error... LVE_DeletePaymente." :"";
//
//			 
//			 sqldel = ""
//				 + "DELETE FROM c_payment "
//				 + "WHERE  c_payment_id = '"+p_C_Payment_ID+"' ";
//			 no2 = DB.executeUpdate(sqldel, null);
//			 log.fine("LVE_DeletePaymente -> c_payment Delete #" + no2);
//			 msg+= (no2<0) ?" Error... LVE_DeletePaymente." :"";
//
//			 
//				
//			
//
//		}
//		
//		catch (Exception e)
//		{
//			log.log(Level.SEVERE, sql, e);
//		}
//		
//		return (msg=="") ? "Proceso completado. " : msg ;
//	
//	}	
	
	
		
	
}	
