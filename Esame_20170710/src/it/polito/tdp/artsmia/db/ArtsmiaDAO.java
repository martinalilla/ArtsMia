package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.EsibizioniComuni;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects(Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
				idMap.put(artObj.getId(), artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <EsibizioniComuni> esibizioni( Map<Integer, ArtObject> idMap, ArtObject a) {
		String sql ="SELECT count(eo2.exhibition_id) AS cnt, eo2.object_id AS id "+
				"FROM exhibition_objects eo1, exhibition_objects eo2 "+
				"WHERE eo1.exhibition_id=eo2.exhibition_id "+
				"AND eo1.object_id=? "+
				"AND eo2.object_id>eo1.object_id "+
				"GROUP BY eo2.object_id ";

				/*"SELECT COUNT(*) AS cnt "+
				"FROM exhibition_objects eo1, exhibition_objects eo2 "+
				"WHERE eo1.exhibition_id=eo2.exhibition_id "+
				"AND eo1.object_id=? AND eo2.object_id=? ";*/
		int conteggio=0;
		Connection conn = DBConnect.getConnection();
		List<EsibizioniComuni> lista =new LinkedList<EsibizioniComuni>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			/*st.setInt(2, a2.getId());*/
			ResultSet res = st.executeQuery();
			while (res.next()) {
				conteggio=res.getInt("cnt");
				EsibizioniComuni e=new EsibizioniComuni(a, idMap.get(res.getInt("id")), conteggio);
				lista.add(e);

				
			}
			conn.close();
			return lista;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
