package cn.eid.sample.idspsdk.common;

/**
 * @author ywc
 * @created 2011-2-18 ÉÏÎç10:45:45
 */
public class PubKey {
	public String e ;
	public String n ;
    public String s ;
	public long sessionKeyIndex;
    public long Alg;

	public String getE() {
		return e;
	}

	public void setE(String e) {
		//this.e = Arrays.copyOf(e, this.e.length);
		this.e = e;
	}

	public String getN() {
		return n;
	}

	public void setN(String n) {
		//this.n = Arrays.copyOf(n, this.n.length);
		this.n = n;
	}

	public String getS() {
	   return s;
	}
	
	public void setS(String s) {
		this.s = s;
	}
	
	public long getSessionKeyIndex(){
		return sessionKeyIndex;
	}
	
	public void setSessionKeyIndex(long index){
		sessionKeyIndex = index;
	}
	
	public long getAlg(){
		return Alg;
	}
	
	public void setAlg(long alg) {
		this.Alg=alg;
	}
	
}
