package alex.geometry.base;

public class LineComplex {
	/*parametric, 
	 x = <xc> + <xt>*t
	 y = <yc> + <yt>*t
	 z = <zc> + <zt>*t
	*/
	public double xc, xt;
	public double yc, yt;
	public double zc, zt;
	
	
	public LineComplex(Vertex p, Vector v){
		this.xc = p.x;
		this.yc = p.y;
		this.zc = p.z;
		this.xt = v.x;
		this.yt = v.y;
		this.zt = v.z;
	}
	
	public LineComplex(Vertex point1, Vertex point2){
		this.xc = point1.x;
		this.yc = point1.y;
		this.zc = point1.z;
		this.xt = point2.x - point1.x;
		this.yt = point2.y - point1.y;
		this.zt = point2.z - point1.z;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("x=").append(xc).append("+").append(xt).append("t\n");
		sb.append("y=").append(yc).append("+").append(yt).append("t\n");
		sb.append("z=").append(zc).append("+").append(zt).append("t");
		return sb.toString();
	}
}
