package kr.ac.mjc.sungbin.spring.day1.class06.componentscan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.ac.mjc.sungbin.spring.day1.class06.common.Board;
import kr.ac.mjc.sungbin.spring.day1.class06.common.DbUtils;

/**
 * p.118-122 수정
 * 
 * @author Jacob
 */
@Component
public class BoardDao {

	private final String LIST_BOARDS = "select seq, title, left(regdate,16) regdate, writer, cnt from board order by seq desc limit ?,?";
	private final String GET_BOARD = "select seq, title,content, left(regdate,16) regdate, writer, cnt from board where seq=?";
	private final String ADD_BOARD = "insert board(title, content, writer) values(?,?,?)";
	private final String UPDATE_BOARD = "update board set title=?, content=? where seq=?";
	private final String DELETE_BOARD = "delete from board where seq=?";

	@Autowired
	private DbUtils dbUtils;

	/**
	 * 게시글 목록 조회
	 * 
	 * @param page  페이지
	 * @param count 갯수
	 */
	public List<Board> listBoards(int count, int page) {
		int offset = (page - 1) * count; // 목록 가져올 시작점
		return dbUtils.list(LIST_BOARDS, (rs) -> {
			Board board = new Board();
			board.setSeq(rs.getString("seq"));
			board.setTitle(rs.getString("title"));
			board.setRegdate(rs.getString("regdate"));
			board.setWriter(rs.getString("writer"));
			board.setCnt(rs.getInt("cnt"));
			return board;
		}, offset, count);
	}

	/**
	 * 게시글 1개 조회
	 */
	public Board getBoard(String seq) {
		return dbUtils.get(this.GET_BOARD, (rs) -> {
			Board board = new Board();
			board.setSeq(rs.getString("seq"));
			board.setTitle(rs.getString("title"));
			board.setContent(rs.getString("content"));
			board.setRegdate(rs.getString("regdate"));
			board.setWriter(rs.getString("writer"));
			board.setCnt(rs.getInt("cnt"));
			return board;
		}, seq);
	}

	/**
	 * 게시글 추가
	 */
	public int addBoard(Board board) {
		return dbUtils.update(ADD_BOARD, board.getTitle(), board.getContent(),
				board.getWriter());
	}

	/**
	 * 게시글 수정
	 */
	public int updateBoard(Board board) {
		return dbUtils.update(UPDATE_BOARD, board.getTitle(),
				board.getContent(), board.getSeq());
	}

	/**
	 * 게시글 삭제
	 */
	public int deleteBoard(String seq) {
		return dbUtils.update(DELETE_BOARD, seq);
	}
}