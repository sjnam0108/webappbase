package kr.co.bbmc.models.srs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import kr.co.bbmc.utils.Util;


public class CntState{ 
	
	public String getnomal() { 
		return nomal; } 
	
	public void setNomal(String nomal) { 
		this.nomal = nomal; } 
	
	public String getlc_off() {
		return lc_off; }
	
	public void setlc_off(String lc_off) { 
		lc_off = lc_off; } 
	public String getsr_all_off() { 
		return sr_all_off; } 
	public void setsr_all_off(String sr_all_off) {
		sr_all_off = sr_all_off; } 
	public String getsr_off() { 
		return sr_off; } 
	public void setsr_off(String sr_off) {
		this.sr_off = sr_off; } 
	public String getuniden() { 
		return uniden; } 
	public void uniden(String uniden) {
		this.uniden = uniden; } 
	String nomal; 
	String lc_off; 
	String sr_all_off; 
	String sr_off; 
	String uniden; }
