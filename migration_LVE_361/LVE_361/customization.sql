// Original contribution by Rafael Tomás Salazar Colmenárez - rsalazar@dcsla.com --- rtsc08@gmail.com  for Double Click Sistemas C.A.

update ad_column set callout='org.doubleclick.callout.LVE_Customization.generationTaxID'  where AD_Column_ID=2909;

INSERT INTO adempiere.ad_entitytype(entitytype, ad_client_id, ad_org_id, ad_entitytype_id, isactive, created, createdby, updated, updatedby, name, description, help, version, modelpackage, classpath, processing) 
    VALUES('DCS', 0, 0, (select max(ad_entitytype_id)+1 from ad_entitytype), 'Y', getdate(), 0, getdate(), 0, 'DoubleClickSistemas', 'DoubleClickSistemas', 'DoubleClickSistemas', '0.1', 'org.doubleclick.model', '', 'N');


