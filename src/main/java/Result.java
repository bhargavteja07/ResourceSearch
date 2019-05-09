import java.util.Date;
public class Result {
	double search_time;
	int total_dropped;
	double wait_time;
	double global_time;
	Date curr_time;
	int assigned_resources;
	int total_waiting;

	public Result(double global_time, Date curr_time) {
		this.search_time = 0;
		this.total_dropped = 0;
		this.wait_time = 0;
		this.global_time = global_time;
		this.curr_time = curr_time;
		this.assigned_resources = 0;
		this.total_waiting = 0;
	}
}
