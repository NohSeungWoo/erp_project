package com.spring.finalProject.model;

public class VacationVO_SKS {
	
		private String seq;
		private String name;
		private String fk_employeeid;
		private String annual;
		private String vacation;
		private String start_date;
		private String end_date;
		
		public VacationVO_SKS() {}
		
		public VacationVO_SKS( String seq, String name, String fk_employeeid, String annual,String vacation,String start_date,String end_date) {
			this.seq = seq;
			this.name = name;
			this.fk_employeeid = fk_employeeid;
			
			this.annual = annual;
			this.vacation = vacation;
			this.start_date = start_date;
			this.end_date= end_date;
		}

		public String getSeq() {
			return seq;
		}

		public void setSeq(String seq) {
			this.seq = seq;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}		

		public String getFk_employeeid() {
			return fk_employeeid;
		}

		public void setFk_employeeid(String fk_employeeid) {
			this.fk_employeeid = fk_employeeid;
		}

		public String getAnnual() {
			return annual;
		}

		public void setAnnual(String annual) {
			this.annual = annual;
		}

		public String getVacation() {
			return vacation;
		}

		public void setVacation(String vacation) {
			this.vacation = vacation;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		

			
	}


