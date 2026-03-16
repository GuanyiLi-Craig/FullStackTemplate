#!/usr/bin/env python3
"""
LangGraph Agent Template - Single-file bundle.

This file contains the entire project as commented-out source code.
The decoder below parses these comments to reconstruct the project.

    python langgraph-agent-bundle.py [target_dir]

Default target_dir is ./langgraph-agent

Then:
    cd langgraph-agent
    cp .env.example .env   # fill in Azure OpenAI creds
    uv sync
    uv run python -m agent
"""

# ===========================================================================
# Project source
# ===========================================================================
#
# -----------------------pyproject.toml-----------------------
# [project]
# name = "langgraph-agent"
# version = "0.1.0"
# description = "LangGraph agent template deployable to AWS ECS or Lambda"
# readme = "README.md"
# requires-python = ">=3.10"
# dependencies = [
#     "langgraph>=0.4",
#     "langchain-openai>=0.3",
#     "langchain-core>=0.3",
#     "pydantic>=2.0",
#     "pydantic-settings>=2.0",
#     "httpx>=0.27",
#     "aws-lambda-powertools>=3.0",
# ]
#
# [project.optional-dependencies]
# dev = [
#     "pytest>=8.0",
#     "pytest-asyncio>=0.24",
#     "ruff>=0.8",
# ]
#
# [tool.ruff]
# target-version = "py310"
# line-length = 120
#
# [tool.ruff.lint]
# select = ["E", "F", "I", "N", "W", "UP"]
#
# [tool.pytest.ini_options]
# asyncio_mode = "auto"
# testpaths = ["tests"]
#
# -----------------------.env.example-----------------------
# # Azure OpenAI (required)
# AZURE_OPENAI_API_KEY=your-api-key-here
# AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
# AZURE_OPENAI_API_VERSION=2024-12-01-preview
# AZURE_OPENAI_DEPLOYMENT=gpt-4o
#
# # Agent settings
# AGENT_MAX_ITERATIONS=10
# AGENT_SYSTEM_PROMPT=You are a helpful assistant. Use the available tools when needed to answer the user's question accurately.
#
# # Runtime mode: "ecs" or "lambda"
# RUNTIME_MODE=ecs
#
# -----------------------.dockerignore-----------------------
# .git
# .venv
# __pycache__
# *.pyc
# .env
# .ruff_cache
# .pytest_cache
# tests/
# *.md
#
# -----------------------Dockerfile-----------------------
# # ---------- build stage ----------
# FROM ghcr.io/astral-sh/uv:python3.11-bookworm-slim AS builder
#
# WORKDIR /app
#
# # Copy dependency manifests first for layer caching
# COPY pyproject.toml uv.lock* ./
# RUN uv sync --no-dev --frozen 2>/dev/null || uv sync --no-dev
#
# # Copy source
# COPY src/ src/
#
# # ---------- ECS runtime ----------
# FROM builder AS ecs
#
# ENV RUNTIME_MODE=ecs
# ENTRYPOINT ["uv", "run", "python", "-m", "agent.interfaces.ecs"]
#
# # ---------- Lambda runtime ----------
# # Uses AWS Lambda Web Adapter for a container-based Lambda.
# # Alternatively, swap for the official AWS Lambda Python base image
# # and set CMD to "agent.lambda_handler.handler".
# FROM builder AS lambda
#
# # Install the AWS Lambda Runtime Interface Client
# RUN uv pip install awslambdaric --system
#
# ENV RUNTIME_MODE=lambda
# ENTRYPOINT ["python", "-m", "awslambdaric"]
# CMD ["agent.interfaces.lambda_handler.handler"]
#
# -----------------------README.md-----------------------
# # LangGraph Agent Template
#
# A production-ready LangGraph agent with an **orchestrator LLM node** and **tool-call node**, deployable to **AWS ECS** (as a task) or **AWS Lambda**.
#
# ## Architecture
#
# ```
# START → orchestrator (Azure OpenAI) ──┐
#              ▲                         │
#              │                    has tool calls?
#              │                    ├─ yes → tool_executor ─┘
#              │                    └─ no  → END
# ```
#
# The orchestrator calls Azure OpenAI with the conversation history and bound tools. If the model returns tool calls, they are executed by `tool_executor` and the loop repeats. A configurable iteration cap prevents runaway loops.
#
# ## Quick start
#
# ```bash
# # Install uv (if needed)
# curl -LsSf https://astral.sh/uv/install.sh | sh
#
# # Install dependencies
# uv sync
#
# # Copy and fill in your Azure OpenAI credentials
# cp .env.example .env
#
# # Start the interactive REPL
# uv run python -m agent
# ```
#
# ## Project structure
#
# The project is split into **core** (agent logic) and **interfaces** (I/O adapters).
# The core is identical regardless of how the agent is invoked.
#
# ```
# src/agent/
# ├── __init__.py              # Package root, exports Agent + build_graph
# ├── __main__.py              # `python -m agent` → launches CLI REPL
# ├── core.py                  # Agent class — the single entry point for all runtimes
# ├── config.py                # Pydantic settings from env vars
# ├── state.py                 # AgentState TypedDict
# ├── tools.py                 # Example tools + ALL_TOOLS registry
# ├── nodes.py                 # orchestrator & tool_executor graph nodes
# ├── graph.py                 # LangGraph graph wiring
# └── interfaces/
#     ├── cli.py               # Local debug REPL (multi-turn, coloured output)
#     ├── ecs.py               # ECS task entry point (env var / stdin)
#     └── lambda_handler.py    # AWS Lambda handler (API Gateway / direct)
# ```
#
# ## Local development
#
# ```bash
# # Interactive REPL (multi-turn conversation)
# uv run python -m agent
#
# # Single-shot via env var
# AGENT_INPUT="What is sqrt(144)?" uv run python -m agent.interfaces.ecs
#
# # Single-shot via stdin JSON
# echo '{"message": "What time is it?"}' | uv run python -m agent.interfaces.ecs
# ```
#
# ## Deployment
#
# ### ECS
#
# ```bash
# docker build --target ecs -t langgraph-agent:ecs .
# docker run --env-file .env langgraph-agent:ecs
# ```
#
# ### Lambda
#
# ```bash
# docker build --target lambda -t langgraph-agent:lambda .
# # Push to ECR, then create a Lambda function using the container image.
# ```
#
# ## Adding tools
#
# 1. Define a new function in `src/agent/tools.py` decorated with `@tool`.
# 2. Append it to the `ALL_TOOLS` list.
#
# The orchestrator and tool executor automatically pick up everything in `ALL_TOOLS`.
#
# ## Tests
#
# ```bash
# uv sync --group dev
# uv run pytest
# ```
#
# -----------------------src/agent/__init__.py-----------------------
# """LangGraph agent with orchestrator LLM and tool-call nodes."""
#
# from agent.core import Agent
# from agent.graph import build_graph
#
# __all__ = ["Agent", "build_graph"]
#
# -----------------------src/agent/__main__.py-----------------------
# """Allow `python -m agent` to launch the CLI REPL."""
#
# from agent.interfaces.cli import main
#
# main()
#
# -----------------------src/agent/config.py-----------------------
# """Configuration loaded from environment variables."""
#
# from pydantic_settings import BaseSettings
#
#
# class Settings(BaseSettings):
#     """Application settings sourced from env vars / .env file."""
#
#     # Azure OpenAI
#     azure_openai_api_key: str
#     azure_openai_endpoint: str
#     azure_openai_api_version: str = "2024-12-01-preview"
#     azure_openai_deployment: str = "gpt-4o"
#
#     # Agent behaviour
#     agent_max_iterations: int = 10
#     agent_system_prompt: str = (
#         "You are a helpful assistant. Use the available tools when needed "
#         "to answer the user's question accurately."
#     )
#
#     # Runtime mode: "ecs" or "lambda"
#     runtime_mode: str = "ecs"
#
#     model_config = {"env_file": ".env", "env_file_encoding": "utf-8"}
#
#
# settings = Settings()  # type: ignore[call-arg]
#
# -----------------------src/agent/state.py-----------------------
# """Agent state definition shared across all graph nodes."""
#
# from __future__ import annotations
#
# import operator
# from typing import Annotated, TypedDict
#
# from langchain_core.messages import BaseMessage
#
#
# class AgentState(TypedDict):
#     """The state that flows through the LangGraph graph.
#
#     Attributes:
#         messages: Accumulated conversation messages (append-only via operator.add).
#         iteration_count: Safety counter to cap tool-call loops.
#     """
#
#     messages: Annotated[list[BaseMessage], operator.add]
#     iteration_count: int
#
# -----------------------src/agent/tools.py-----------------------
# """Example tools that the agent can invoke.
#
# Add, remove, or replace these with your own domain-specific tools.
# Each tool is a plain function decorated with @tool from langchain_core.
# """
#
# from __future__ import annotations
#
# import math
# from datetime import datetime, timezone
#
# from langchain_core.tools import tool
#
#
# @tool
# def get_current_time() -> str:
#     """Return the current UTC date and time in ISO-8601 format."""
#     return datetime.now(timezone.utc).isoformat()
#
#
# @tool
# def calculator(expression: str) -> str:
#     """Evaluate a mathematical expression and return the result.
#
#     Supports basic arithmetic, powers, sqrt, log, pi, e, etc.
#     Example: "sqrt(2) + 3 ** 2"
#     """
#     # Restricted namespace for safety
#     allowed_names = {
#         k: getattr(math, k)
#         for k in dir(math)
#         if not k.startswith("_")
#     }
#     allowed_names["abs"] = abs
#     allowed_names["round"] = round
#     try:
#         result = eval(expression, {"__builtins__": {}}, allowed_names)  # noqa: S307
#         return str(result)
#     except Exception as exc:
#         return f"Error evaluating expression: {exc}"
#
#
# @tool
# def search_knowledge_base(query: str) -> str:
#     """Search an internal knowledge base for relevant information.
#
#     This is a **stub** — replace the body with a real retrieval call
#     (e.g. vector-store lookup, API request, database query).
#     """
#     # TODO: Replace with actual retrieval logic
#     return (
#         f"[stub] No results found for '{query}'. "
#         "Implement search_knowledge_base with your own data source."
#     )
#
#
# # ---------------------------------------------------------------------------
# # Registry: import this list wherever you wire tools into the graph.
# # ---------------------------------------------------------------------------
# ALL_TOOLS = [get_current_time, calculator, search_knowledge_base]
#
# -----------------------src/agent/nodes.py-----------------------
# """Graph node implementations.
#
# Two nodes make up the agent loop:
#   1. **orchestrator** – calls the LLM (Azure OpenAI) with the current messages
#      and available tools.  The LLM decides whether to answer directly or
#      request one-or-more tool calls.
#   2. **tool_executor** – executes every tool call the LLM requested and
#      appends the results back as ToolMessages.
# """
#
# from __future__ import annotations
#
# import logging
#
# from langchain_core.messages import AIMessage, SystemMessage, ToolMessage
# from langchain_openai import AzureChatOpenAI
# from langgraph.prebuilt import ToolNode
#
# from agent.config import settings
# from agent.state import AgentState
# from agent.tools import ALL_TOOLS
#
# logger = logging.getLogger(__name__)
#
# # ---------------------------------------------------------------------------
# # LLM singleton (bound with tools so it can emit tool_calls)
# # ---------------------------------------------------------------------------
# _llm: AzureChatOpenAI | None = None
#
#
# def _get_llm() -> AzureChatOpenAI:
#     """Lazily initialise the Azure OpenAI chat model."""
#     global _llm  # noqa: PLW0603
#     if _llm is None:
#         _llm = AzureChatOpenAI(
#             azure_endpoint=settings.azure_openai_endpoint,
#             azure_deployment=settings.azure_openai_deployment,
#             api_version=settings.azure_openai_api_version,
#             api_key=settings.azure_openai_api_key,
#         )
#     return _llm
#
#
# def get_llm_with_tools() -> AzureChatOpenAI:
#     """Return the LLM instance with tools bound."""
#     return _get_llm().bind_tools(ALL_TOOLS)
#
#
# # ---------------------------------------------------------------------------
# # Node 1 — Orchestrator (LLM)
# # ---------------------------------------------------------------------------
# def orchestrator(state: AgentState) -> dict:
#     """Invoke the LLM with the conversation so far.
#
#     Prepends the system prompt if the conversation doesn't already
#     contain one, then calls the model.  Returns the AI message
#     (which may or may not contain tool_calls) and bumps the
#     iteration counter.
#     """
#     messages = list(state["messages"])
#
#     # Ensure system prompt is present
#     if not messages or not isinstance(messages[0], SystemMessage):
#         messages.insert(0, SystemMessage(content=settings.agent_system_prompt))
#
#     llm = get_llm_with_tools()
#     response: AIMessage = llm.invoke(messages)
#
#     logger.info(
#         "Orchestrator produced response with %d tool call(s)",
#         len(response.tool_calls) if hasattr(response, "tool_calls") else 0,
#     )
#
#     return {
#         "messages": [response],
#         "iteration_count": state.get("iteration_count", 0) + 1,
#     }
#
#
# # ---------------------------------------------------------------------------
# # Node 2 — Tool executor
# # ---------------------------------------------------------------------------
# # Uses LangGraph's built-in ToolNode which automatically:
# #   • reads tool_calls from the last AIMessage
# #   • invokes matching tools
# #   • returns ToolMessage(s) appended to state.messages
# tool_executor = ToolNode(tools=ALL_TOOLS)
#
# -----------------------src/agent/graph.py-----------------------
# """LangGraph graph definition.
#
# The graph implements a standard ReAct-style loop:
#
#     ┌─────────────┐
#     │  START       │
#     └──────┬──────┘
#            ▼
#     ┌─────────────┐
#     │ orchestrator │◄──────────────┐
#     └──────┬──────┘               │
#            ▼                      │
#       has tool calls?             │
#        ├─ yes ──► tool_executor ──┘
#        └─ no  ──► END
#
# A safety check ensures the loop stops after `agent_max_iterations`.
# """
#
# from __future__ import annotations
#
# from langgraph.graph import END, StateGraph
#
# from agent.config import settings
# from agent.nodes import orchestrator, tool_executor
# from agent.state import AgentState
#
#
# def _should_continue(state: AgentState) -> str:
#     """Routing function after the orchestrator node.
#
#     Returns:
#         "tool_executor" if the last AI message contains tool calls
#         and we haven't hit the iteration cap.
#         "end" otherwise.
#     """
#     last_message = state["messages"][-1]
#
#     # Safety: cap iterations to prevent runaway loops
#     if state.get("iteration_count", 0) >= settings.agent_max_iterations:
#         return "end"
#
#     if hasattr(last_message, "tool_calls") and last_message.tool_calls:
#         return "tool_executor"
#
#     return "end"
#
#
# def build_graph() -> StateGraph:
#     """Construct and compile the agent graph.
#
#     Returns a compiled LangGraph ``CompiledGraph`` ready to be invoked.
#     """
#     graph = StateGraph(AgentState)
#
#     # --- nodes ---
#     graph.add_node("orchestrator", orchestrator)
#     graph.add_node("tool_executor", tool_executor)
#
#     # --- edges ---
#     graph.set_entry_point("orchestrator")
#
#     graph.add_conditional_edges(
#         "orchestrator",
#         _should_continue,
#         {
#             "tool_executor": "tool_executor",
#             "end": END,
#         },
#     )
#
#     # After tool execution, always loop back to the orchestrator
#     graph.add_edge("tool_executor", "orchestrator")
#
#     return graph.compile()
#
# -----------------------src/agent/core.py-----------------------
# """Core agent interface — the single entry point for all runtimes.
#
# Every interface (CLI, ECS, Lambda) calls into this module.
# This keeps the agent logic in one place and guarantees identical
# behaviour regardless of how it's invoked.
# """
#
# from __future__ import annotations
#
# import logging
# from dataclasses import dataclass, field
#
# from langchain_core.messages import BaseMessage, HumanMessage
#
# from agent.graph import build_graph
#
# logger = logging.getLogger(__name__)
#
#
# @dataclass
# class AgentResponse:
#     """Structured result returned by the agent."""
#
#     answer: str
#     messages: list[BaseMessage] = field(default_factory=list)
#     iteration_count: int = 0
#
#
# class Agent:
#     """Stateful wrapper around the compiled LangGraph graph.
#
#     Holds conversation history so callers don't have to manage it.
#     Use one instance per conversation / request.
#
#     Usage::
#
#         agent = Agent()
#         r1 = agent.send("What time is it?")
#         r2 = agent.send("And what about sqrt(144)?")
#         agent.reset()          # clear history
#     """
#
#     def __init__(self) -> None:
#         self._graph = build_graph()
#         self._messages: list[BaseMessage] = []
#         self._iteration_count: int = 0
#
#     # ------------------------------------------------------------------
#     # Public API
#     # ------------------------------------------------------------------
#
#     def send(self, user_message: str) -> AgentResponse:
#         """Send a user message and return the agent's response.
#
#         Conversation history is preserved across calls for multi-turn.
#         """
#         self._messages.append(HumanMessage(content=user_message))
#
#         result = self._graph.invoke(
#             {
#                 "messages": list(self._messages),
#                 "iteration_count": self._iteration_count,
#             }
#         )
#
#         # Collect new messages produced by the graph
#         new_messages = result["messages"][len(self._messages):]
#         self._messages.extend(new_messages)
#         self._iteration_count = result.get("iteration_count", self._iteration_count)
#
#         final = result["messages"][-1]
#
#         return AgentResponse(
#             answer=final.content,
#             messages=list(self._messages),
#             iteration_count=self._iteration_count,
#         )
#
#     def reset(self) -> None:
#         """Clear conversation history and iteration counter."""
#         self._messages.clear()
#         self._iteration_count = 0
#
#     @property
#     def messages(self) -> list[BaseMessage]:
#         """Current conversation history (read-only copy)."""
#         return list(self._messages)
#
# -----------------------src/agent/interfaces/__init__.py-----------------------
#
# -----------------------src/agent/interfaces/cli.py-----------------------
# """Local interactive REPL for debugging and development.
#
# Usage:
#     uv run python -m agent.interfaces.cli
# """
#
# from __future__ import annotations
#
# import logging
#
# from agent.core import Agent
#
# logging.basicConfig(
#     level=logging.INFO,
#     format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
# )
#
# HELP_TEXT = """
# Commands:
#   /help     Show this help message
#   /clear    Clear conversation history and start over
#   /history  Show raw message history (for debugging)
#   /quit     Exit the agent (also: Ctrl-C, Ctrl-D)
# """.strip()
#
#
# def main() -> None:
#     agent = Agent()
#
#     print("LangGraph Agent  —  local debug REPL")
#     print("Type /help for commands, /quit to exit.\n")
#
#     while True:
#         try:
#             user_input = input("\033[1;36myou:\033[0m ").strip()
#         except (EOFError, KeyboardInterrupt):
#             print("\nBye!")
#             break
#
#         if not user_input:
#             continue
#
#         # ---- slash commands ----
#         if user_input.lower() in ("/quit", "/exit", "/q"):
#             print("Bye!")
#             break
#
#         if user_input.lower() == "/help":
#             print(HELP_TEXT)
#             continue
#
#         if user_input.lower() == "/clear":
#             agent.reset()
#             print("-- conversation cleared --")
#             continue
#
#         if user_input.lower() == "/history":
#             for msg in agent.messages:
#                 role = type(msg).__name__
#                 preview = msg.content[:120] if msg.content else "(empty)"
#                 print(f"  [{role}] {preview}")
#             if not agent.messages:
#                 print("  (empty)")
#             continue
#
#         # ---- send to agent ----
#         try:
#             response = agent.send(user_input)
#         except Exception as exc:
#             print(f"\033[1;31merror:\033[0m {exc}")
#             continue
#
#         print(f"\n\033[1;32magent:\033[0m {response.answer}\n")
#
#
# if __name__ == "__main__":
#     main()
#
# -----------------------src/agent/interfaces/ecs.py-----------------------
# """ECS task entry point.
#
# Reads a single message from an env var or stdin JSON, runs the agent,
# prints the answer, and exits.
#
# Usage:
#     AGENT_INPUT='What time is it?' python -m agent.interfaces.ecs
#     echo '{"message": "Hello"}' | python -m agent.interfaces.ecs
# """
#
# from __future__ import annotations
#
# import json
# import logging
# import sys
#
# from agent.core import Agent
#
# logging.basicConfig(
#     level=logging.INFO,
#     format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
# )
# logger = logging.getLogger(__name__)
#
#
# def _read_input() -> str:
#     """Resolve user input from env var or stdin."""
#     import os
#
#     # Priority 1: env var
#     user_input = os.environ.get("AGENT_INPUT")
#     if user_input:
#         return user_input
#
#     # Priority 2: stdin JSON  {"message": "..."}
#     if not sys.stdin.isatty():
#         payload = json.load(sys.stdin)
#         return payload.get("message", "")
#
#     return ""
#
#
# def main() -> None:
#     user_input = _read_input()
#     if not user_input:
#         print(
#             "Provide input via AGENT_INPUT env var or stdin JSON.",
#             file=sys.stderr,
#         )
#         sys.exit(1)
#
#     logger.info("Running agent with input: %s", user_input[:120])
#
#     agent = Agent()
#     response = agent.send(user_input)
#     print(response.answer)
#
#
# if __name__ == "__main__":
#     main()
#
# -----------------------src/agent/interfaces/lambda_handler.py-----------------------
# """AWS Lambda handler.
#
# Expected event shape (API Gateway proxy or direct invocation):
#     {
#         "body": "{\"message\": \"What time is it?\"}"
#     }
#     — or for direct invocation —
#     {
#         "message": "What time is it?"
#     }
#
# Returns a JSON-serialisable response compatible with API Gateway v2.
# """
#
# from __future__ import annotations
#
# import json
#
# from aws_lambda_powertools import Logger
# from aws_lambda_powertools.utilities.typing import LambdaContext
#
# from agent.core import Agent
#
# logger = Logger(service="langgraph-agent")
#
#
# @logger.inject_lambda_context(log_event=True)
# def handler(event: dict, context: LambdaContext) -> dict:
#     """Lambda entry point."""
#     body = event
#     if "body" in event:
#         body = json.loads(event["body"]) if isinstance(event["body"], str) else event["body"]
#
#     user_message: str = body.get("message", "")
#     if not user_message:
#         return {
#             "statusCode": 400,
#             "body": json.dumps({"error": "Missing 'message' field in request body."}),
#         }
#
#     logger.info("Invoking agent", extra={"input_preview": user_message[:120]})
#
#     agent = Agent()
#     response = agent.send(user_message)
#
#     return {
#         "statusCode": 200,
#         "headers": {"Content-Type": "application/json"},
#         "body": json.dumps({"answer": response.answer}),
#     }
#
# -----------------------tests/__init__.py-----------------------
#
# -----------------------tests/test_graph.py-----------------------
# """Smoke tests for graph construction and core Agent (no LLM calls)."""
#
# from __future__ import annotations
#
# from unittest.mock import MagicMock, patch
#
# from langchain_core.messages import AIMessage, HumanMessage
#
# from agent.tools import ALL_TOOLS
#
#
# # ---------------------------------------------------------------------------
# # Tool unit tests
# # ---------------------------------------------------------------------------
#
# def test_tools_registered():
#     """All example tools should be importable and non-empty."""
#     assert len(ALL_TOOLS) >= 1
#     for t in ALL_TOOLS:
#         assert callable(t.invoke)
#
#
# def test_calculator_tool():
#     from agent.tools import calculator
#
#     result = calculator.invoke({"expression": "2 + 3"})
#     assert result == "5"
#
#
# def test_get_current_time_tool():
#     from agent.tools import get_current_time
#
#     result = get_current_time.invoke({})
#     assert "T" in result  # ISO format contains 'T'
#
#
# # ---------------------------------------------------------------------------
# # Graph-level tests (LLM mocked)
# # ---------------------------------------------------------------------------
#
# @patch("agent.nodes._get_llm")
# def test_graph_builds_and_routes_to_end(mock_llm_factory):
#     """When the LLM returns a plain message (no tool calls), the graph should end."""
#     mock_llm = MagicMock()
#     mock_llm.bind_tools.return_value = mock_llm
#     mock_llm.invoke.return_value = AIMessage(content="Hello!")
#     mock_llm_factory.return_value = mock_llm
#
#     from agent.graph import build_graph
#
#     graph = build_graph()
#     result = graph.invoke(
#         {
#             "messages": [HumanMessage(content="Hi")],
#             "iteration_count": 0,
#         }
#     )
#
#     assert result["messages"][-1].content == "Hello!"
#
#
# # ---------------------------------------------------------------------------
# # Core Agent class tests (LLM mocked)
# # ---------------------------------------------------------------------------
#
# @patch("agent.nodes._get_llm")
# def test_agent_send_returns_response(mock_llm_factory):
#     """Agent.send() should return an AgentResponse with the answer."""
#     mock_llm = MagicMock()
#     mock_llm.bind_tools.return_value = mock_llm
#     mock_llm.invoke.return_value = AIMessage(content="I'm fine!")
#     mock_llm_factory.return_value = mock_llm
#
#     from agent.core import Agent
#
#     agent = Agent()
#     resp = agent.send("How are you?")
#
#     assert resp.answer == "I'm fine!"
#     assert resp.iteration_count >= 1
#
#
# @patch("agent.nodes._get_llm")
# def test_agent_multi_turn_preserves_history(mock_llm_factory):
#     """Conversation history should accumulate across multiple send() calls."""
#     mock_llm = MagicMock()
#     mock_llm.bind_tools.return_value = mock_llm
#     mock_llm.invoke.return_value = AIMessage(content="reply")
#     mock_llm_factory.return_value = mock_llm
#
#     from agent.core import Agent
#
#     agent = Agent()
#     agent.send("first")
#     agent.send("second")
#
#     # Should have: HumanMessage("first"), AIMessage("reply"),
#     #              HumanMessage("second"), AIMessage("reply")
#     human_msgs = [m for m in agent.messages if isinstance(m, HumanMessage)]
#     assert len(human_msgs) == 2
#
#
# @patch("agent.nodes._get_llm")
# def test_agent_reset_clears_history(mock_llm_factory):
#     """Agent.reset() should wipe conversation state."""
#     mock_llm = MagicMock()
#     mock_llm.bind_tools.return_value = mock_llm
#     mock_llm.invoke.return_value = AIMessage(content="ok")
#     mock_llm_factory.return_value = mock_llm
#
#     from agent.core import Agent
#
#     agent = Agent()
#     agent.send("hello")
#     assert len(agent.messages) > 0
#
#     agent.reset()
#     assert len(agent.messages) == 0
#

# ===========================================================================
# Decoder — parses the commented source above into a project directory
# ===========================================================================

import os
import re
import sys


def decode(target_dir: str = "langgraph-agent") -> None:
    target_dir = os.path.abspath(target_dir)
    if os.path.exists(target_dir):
        print(f"Error: '{target_dir}' already exists. Remove it or pick another name.")
        sys.exit(1)

    # Read ourselves
    with open(__file__, "r", encoding="utf-8") as f:
        self_lines = f.readlines()

    sep = "-" * 23
    pattern = re.compile(rf"^# {re.escape(sep)}(.+?){re.escape(sep)}$")

    files: dict[str, list[str]] = {}
    current_path: str | None = None

    for raw_line in self_lines:
        line = raw_line.rstrip("\n")

        # Check for separator
        m = pattern.match(line)
        if m:
            current_path = m.group(1)
            files[current_path] = []
            continue

        if current_path is None:
            continue

        # End of a file block: a line that is just "#" followed by a separator
        # or a non-comment line means we left the block
        if not line.startswith("#"):
            current_path = None
            continue

        # Strip the "# " prefix (or lone "#" for empty lines)
        if line == "#":
            files[current_path].append("")
        elif line.startswith("# "):
            files[current_path].append(line[2:])
        else:
            files[current_path].append(line[1:])

    # Write files
    written = 0
    for rel_path, content_lines in files.items():
        # Remove trailing empty line that was the block separator
        while content_lines and content_lines[-1] == "":
            content_lines.pop()

        content = "\n".join(content_lines)
        if content:
            content += "\n"

        full_path = os.path.join(target_dir, rel_path)
        os.makedirs(os.path.dirname(full_path), exist_ok=True)
        with open(full_path, "w", encoding="utf-8") as f:
            f.write(content)
        written += 1

    print(f"Decoded {written} files into '{target_dir}/'")
    print()
    print("Next steps:")
    print(f"  cd {os.path.basename(target_dir)}")
    print("  cp .env.example .env   # fill in your Azure OpenAI credentials")
    print("  uv sync")
    print("  uv run python -m agent")


if __name__ == "__main__":
    target = sys.argv[1] if len(sys.argv) > 1 else "langgraph-agent"
    decode(target)
